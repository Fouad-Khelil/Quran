package com.example.quran.presentation.splash

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quran.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi


@SuppressLint("MissingPermission")
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun LocationRequestScreen(
    onNavigate: ()-> Unit,
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val permissions = arrayOf(
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION
    )

    var isGpsEnabled by remember { mutableStateOf(false) }
    var isLocationNull by remember { mutableStateOf(false) }
    var requestTimeOut by remember { mutableStateOf(1) }
    var latitude by remember { mutableStateOf(0.0) }
    var longtitude by remember { mutableStateOf(0.0) }


    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
    val locationProvider = LocationServices.getFusedLocationProviderClient(context)


    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            isGpsEnabled = true
            if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isGpsEnabled = true
                if (isConnected) {
                    getLocation(
                        LocationServices.getFusedLocationProviderClient(context),
                        context,
                        onLocationNull = {
                            isLocationNull =true
                        },
                        onLocationSuccess =
                        { lat, lng ->
                            latitude = lat
                            longtitude = lng
                            requestTimeOut -= 1
                        })
                }
            }
        } else {
            Toast.makeText(context, "something is wrong", Toast.LENGTH_SHORT).show()
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
            val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                Toast.makeText(context, "Permission Granted ", Toast.LENGTH_SHORT).show()
                checkLocationSetting(
                    context = context,
                    onDisabled = { intentSenderRequest ->
                        settingResultRequest.launch(intentSenderRequest)
                    },
                    onEnabled = {/* This will call when setting is already enabled */
                        isGpsEnabled = true
                    }
                )
            } else {
                onNavigate()
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }

        }

    LaunchedEffect(Unit) {
        launcher.launch(permissions)
    }


    if (isConnected && isGpsEnabled) {

        Log.d("mylocation", " ------$requestTimeOut")
        getLocation(
            locationProvider,
            context,
            onLocationNull = {
                isLocationNull =true
            },
            onLocationSuccess = { lat, lng ->
                Log.d("mylocation", "this is inside get location$requestTimeOut")
                latitude = lat
                longtitude = lng
            })

        if (latitude != 0.0 && requestTimeOut >= 0) {
            Log.d("mylocation", "location : $latitude $longtitude")
            requestTimeOut -= 1
            //todo save lat and lng
            locationViewModel.saveLocation(latitude, longtitude){
               onNavigate()
            }
            return
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Log.d("mylocation", "****** $longtitude")
                CircularProgressIndicator()
            }
        }

    }
    if(isLocationNull||!isConnected || !isGpsEnabled ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.img_map),
                contentDescription = null
            )

            Text(
                text = "تفقد بيانات الاتصال و تاكد  من تفعيل الموقع لاجل تحديد مواقيت الصلاة الخاصة بمنطقتك",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Button(
                    onClick = {
                        getLocation(
                            locationProvider,
                            context,
                            onLocationNull = {
                                isLocationNull =true
                            },
                            onLocationSuccess = { lat, lng ->
                                latitude = lat
                                longtitude = lng
                            })
                    },
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(text = "اعد المحاولة")
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(onClick = {
                   onNavigate()
                }) {
                    Text(text = "   تجاهل   ")
                }
            }
        }
    }
}


@SuppressLint("MissingPermission")
fun getLocation(
    locationProvider: FusedLocationProviderClient,
    context: Context,
    onLocationSuccess: (Double, Double) -> Unit,
    onLocationNull: () -> Unit
) {
    locationProvider.getCurrentLocation(Priority.PRIORITY_LOW_POWER, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                onLocationSuccess(location.latitude, location.longitude)
            } else {
                onLocationNull()
            }

        }.addOnFailureListener {
            Toast.makeText(
                context,
                "check your gps",
                Toast.LENGTH_SHORT
            ).show()
        }
}

fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {

    val locationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_LOW_POWER
    }

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
        .Builder()
        .addLocationRequest(locationRequest)

    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // ignore here
            }
        }
    }

}

data class LocationDetail(val lat: Double, val lng: Double)
