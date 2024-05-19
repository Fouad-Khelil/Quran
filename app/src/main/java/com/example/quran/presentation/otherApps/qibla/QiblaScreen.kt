package com.example.quran.presentation.otherApps.qibla

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.presentation.splash.checkLocationSetting
import com.example.quran.presentation.splash.getLocation
import com.google.android.gms.location.LocationServices
import kotlin.math.cos
import kotlin.math.sin


const val PHASE = 270f

@Composable
fun QiblaScreen(
    navController: NavController,
    qiblaViewModel: QiblaViewModel = hiltViewModel()
) {

    val directionValue = qiblaViewModel.direction.collectAsState().value
    val currentRotation = remember { mutableStateOf(0f) }

    // Calculate the rotation difference
    val rotationDiff = directionValue - currentRotation.value
    val adjustedRotation = if (rotationDiff > 180) {
        directionValue - 360f
    } else if (rotationDiff < -180) {
        directionValue + 360f
    } else {
        directionValue
    }

    // Update the current rotation
    currentRotation.value = adjustedRotation

    val rotation = animateFloatAsState(
        targetValue = adjustedRotation + PHASE,
        animationSpec = tween(easing = LinearOutSlowInEasing)
    ).value


    val radius = 150.dp
    val radiusInPixels = with(LocalDensity.current) { radius.toPx() }
    val context = LocalContext.current
    var location by remember { mutableStateOf("") }


    val permissions = arrayOf(
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION
    )

    val locationProvider = LocationServices.getFusedLocationProviderClient(context)

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
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
//                        isGpsEnabled = true
                        getLocation(
                            locationProvider,
                            context,
                            onLocationNull = {
//                                isLocationNull = true
                            },
                            onLocationSuccess = { lat, lng ->
                                DataStoreRepository(context).setLatAndLng(lat, lng)
                                (context as? Activity)?.recreate()
                            })
                    }
                )
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }

        }



    LaunchedEffect(key1 = true) {
        location = DataStoreRepository(context).getLocationName(context)
        if (location.isEmpty()) {
            launcher.launch(permissions)
        }else{
            qiblaViewModel.initSensors()
        }

//        checkAndRequestLocationPermissions(context, permissions, launcherMultiplePermissions) {
//            checkLocationSetting(
//                context = context,
//                onDisabled = { intentSenderRequest ->
//                    settingResultRequest.launch(intentSenderRequest)
//                },
//                onEnabled = {/* This will call when setting is already enabled */
//                    getLocation(locationProvider, context) { lat, lng ->
//                        launch {
//                            DataStoreRepository(context).setLatAndLng(lat, lng, context)
//                        }
//                        Log.d("location", "QiblaScreen, lat: $lat lng: $lng")
//                    }
//                }
//            )
//        }
    }

    if (qiblaViewModel.isQiblaSensorsExist) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "$location".ifEmpty { "لم يتم الحصول على الموقع الجغرافي" },
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = "",//must to be updated
                    fontSize = 36.sp,
                    color = Color.White
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_compass),
                contentDescription = null,
                modifier = Modifier
                    .size(280.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.img_kaaba),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .graphicsLayer {
                        // Apply the rotation around the center of the icon
                        translationX =
                            (radiusInPixels * cos(Math.toRadians(rotation.toDouble()))).toFloat()
                        translationY =
                            (radiusInPixels * sin(Math.toRadians(rotation.toDouble()))).toFloat()
                        // Rotate the icon itself
                        rotationZ = rotation + 90f
                    },
            )

            Image(
                painter = painterResource(id = R.drawable.ic_qibla), // Replace with your icon
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .graphicsLayer {
                        // Apply rotation to the icon
                        rotationZ = rotation + 45f
                    }
            )


            Image(
                painter = painterResource(id = R.drawable.img_background_compass),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.align(Alignment.BottomCenter),
            )

//        Text(text = "direction is :" + qiblaViewModel.direction.collectAsState().value.toString())
        }

    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "خاصية القبلة غير متوفرة في هاتفك")
        }
    }

}


fun checkAndRequestLocationPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    onSuccess: () -> Unit
) {
    if (
        permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        onSuccess()
        // Use location because permissions are already granted
    } else {
        // Request permissions
        launcher.launch(permissions)
    }
}