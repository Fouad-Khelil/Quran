package com.example.quran.presentation.otherApps.qibla

import android.hardware.GeomagneticField
import android.hardware.SensorManager
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.sensor_manager.AccelerometerSensor
import com.example.quran.sensor_manager.MagneticFieldSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@Suppress("DEPRECATION")
@HiltViewModel
class QiblaViewModel @Inject constructor(
    val accelerometerSensor: AccelerometerSensor,
    val magneticFieldSensor: MagneticFieldSensor ,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {


    val isQiblaSensorsExist = accelerometerSensor.doesSensorExist && magneticFieldSensor.doesSensorExist


    private val _direction = MutableStateFlow(0f)
    val direction: StateFlow<Float> = _direction


    private var floatGravity = FloatArray(3)
    private var floatMagneticField = FloatArray(3)
    private var floatOrientation = FloatArray(3)
    private var floatRotationMatrix = FloatArray(9)

    fun initSensors() {

        accelerometerSensor.startListening()
        magneticFieldSensor.startListening()

        accelerometerSensor.setOnSensorValuesChangedListener { values ->
            floatGravity = values.toFloatArray()
            SensorManager.getRotationMatrix(
                floatRotationMatrix,
                null,
                floatGravity,
                floatMagneticField
            )
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation)
            var azimuth = Math.toDegrees(floatOrientation[0].toDouble()).toFloat() // orientation

             val head  = (azimuth + 360) % 360
            val loc = dataStoreRepository.getLatAndLng()
            Log.d("saveLocation", "from qibla lat : ${loc.lat} , long :${loc.lng}")

            val userLocation = Location("")
            userLocation.latitude = loc.lat
            userLocation.longitude = loc.lng
            updateQiblaDirection( userLocation, head)
        }

        magneticFieldSensor.setOnSensorValuesChangedListener { values ->
            floatMagneticField = values.toFloatArray()
            SensorManager.getRotationMatrix(
                floatRotationMatrix,
                null,
                floatGravity,
                floatMagneticField
            )
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation)
        }

    }

    override fun onCleared() {
        accelerometerSensor.stopListening()
        magneticFieldSensor.stopListening()
    }

    fun updateQiblaDirection(userLocation: Location? , head : Float)  {

        userLocation?.let {
            val kaabaLatitude = 21.422487 // Kaaba latitude
            val kaabaLongitude = 39.826206 // Kaaba longitude

            val destinationLoc = Location("Kaaba")
            destinationLoc.latitude = kaabaLatitude
            destinationLoc.longitude = kaabaLongitude

            var bearTo = it.bearingTo(destinationLoc)

            val geoField = GeomagneticField(
                it.latitude.toFloat(),
                it.longitude.toFloat(),
                it.altitude.toFloat(),
                System.currentTimeMillis()
            )

            //head = The angle that you've rotated your phone from true north.
            var myhead = head - geoField.declination
            if (bearTo < 0) {
                bearTo += 360
            }

            //bearTo = The angle from true north to the destination location
            // from the point we're your currently standing
            var direc = bearTo - myhead
            if (direc < 0f) {
                direc += 360f
            }
            _direction.value = direc
        }
    }

}
