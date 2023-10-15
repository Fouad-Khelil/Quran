package com.example.quran.presentation.auditableQuran

import android.os.CountDownTimer
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quran.exoplayer.toSora
import com.example.quran.others.formatLong
import com.example.quran.presentation.auditableQuran.viewmodels.AllSoraAudioViewModel
import com.example.quran.presentation.auditableQuran.viewmodels.AudioPlayerViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AudioPlayerScreen(
    rewayah: String = "",
    navController: NavController,
    allSoraAudioViewModel: AllSoraAudioViewModel = hiltViewModel(),
    audioPlayerViewModel: AudioPlayerViewModel = hiltViewModel()
) {
//    val  navBackStackEntry by navController.currentBackStackEntryAsState()
//    val isFromHome = navBackStackEntry?.destination?.parent?.route  == ItemBottomNav.Home.route

    var isRepeadModeClicked by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var isTimePicked by remember { mutableStateOf(false) }
    val timeData = remember { mutableStateOf(300 * 1000L) }
    var pickedTime by remember { mutableStateOf(300 * 1000L) }

    var showPlayerFullScreen by remember { mutableStateOf(false) }  // for sliding the player
    var sliderIsChanging by remember { mutableStateOf(false) }
    var localSliderValue by remember { mutableStateOf(0f) }

    val sliderProgress =
        if (sliderIsChanging) localSliderValue else audioPlayerViewModel.currentPlayerPosition


    val soraMedia = allSoraAudioViewModel.currentPlayingSora.value
    val sora = soraMedia?.toSora()


    LaunchedEffect(key1 = true) {
        showPlayerFullScreen = true
        audioPlayerViewModel.updateCurrentPlaybackPosition()
    }

    val swipeableState = rememberSwipeableState(initialValue = 0)
    val endAnchor = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0, endAnchor to 1
    )

    val timer = object : CountDownTimer(pickedTime, 1000) {
        override fun onTick(millisUntilFinished: Long) {
//            val minutesLeft = millisUntilFinished / (1000 * 60) // remember that we have a function that we can use
//            val secondsLeft = (millisUntilFinished / 1000) % 60
            timeData.value = millisUntilFinished
        }

        override fun onFinish() {
            isTimePicked = false
            allSoraAudioViewModel.playOrToggleSora(sora!!, true)
        }
    }

    DisposableEffect(key1 = isTimePicked) {
        if (isTimePicked) {
            timer.start()
        }
        onDispose {
            timer.cancel()
        }
    }

    AnimatedVisibility(
        visible = showPlayerFullScreen,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(
            targetOffsetY = { it },
        )
    ) {

        if (soraMedia != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 18.dp )
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.34f) },
                        orientation = Orientation.Vertical
                    ), verticalArrangement = Arrangement.SpaceBetween
            ) {

                if (swipeableState.currentValue >= 1) {
                    LaunchedEffect("key") {
//                    mainViewModel.showPlayerFullScreen = false
                        showPlayerFullScreen = false
                        navController.popBackStack()
                    }
                }

                AudioTopBar(
                    onBackIconClick = {
                        showPlayerFullScreen = false
                        navController.popBackStack()
                    },
                    onDownloadIconClick = {},
                )

                Column {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(sora?.imageUrl)
                            .crossfade(true)
                            .build(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                            .heightIn(min = 200.dp, max = 360.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "القارئ ${sora!!.reciter}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                    Text(
                        text = "${sora.name} - رواية $rewayah",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }


                Column {
                    RepeatControllerBar(isRepeatModeClicked = isRepeadModeClicked, onRepeatSurah = {
                        Log.d("repeatonce", "AudioPlayerScreen: ")
                        isRepeadModeClicked = !isRepeadModeClicked
                        allSoraAudioViewModel.repeatSoraOnce(isRepeadModeClicked)
                    }, onTimePickerClick = {
                        showTimePickerDialog = !showTimePickerDialog
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                    SliderItem(playbackProgress = sliderProgress,
                        currentTime = audioPlayerViewModel.currentPlaybackFormattedPosition,
                        totalTime = audioPlayerViewModel.currentSoraFormattedPosition,
                        onSliderChange = { newPosition ->
                            localSliderValue = newPosition
                            sliderIsChanging = true
                        },
                        onSliderChangeFinished = {
                            allSoraAudioViewModel.seekTo(audioPlayerViewModel.currentSoraDuration * localSliderValue)
                            sliderIsChanging = false
                        })
                    Spacer(modifier = Modifier.height(8.dp))

                    AudioControleBar(isPlay = allSoraAudioViewModel.soraIsPlaying, onPlay = {
                        if (sora != null) {
                            allSoraAudioViewModel.playOrToggleSora(sora, true)
                        }
                    }, onSkipForward = {
                        allSoraAudioViewModel.skipToNextSora()
                    }, onSkipPrevious = {
                        allSoraAudioViewModel.skipToPreviousSora()
                    }, onReplayFiveForward = {
                        audioPlayerViewModel.currentPlaybackPosition.let { currentPosition ->
                            allSoraAudioViewModel.seekTo(currentPosition + 5 * 1000f)
                        }
                    }, onReplayFiveBackward = {
                        audioPlayerViewModel.currentPlaybackPosition.let { currentPosition ->
                            allSoraAudioViewModel.seekTo(currentPosition - 5 * 1000f)
                        }
                    })
                }
            }


            if (showTimePickerDialog) {
                TimePickDialog(countDownTime = formatLong(timeData.value),
                    isPicked = isTimePicked,
                    onDismiss = {
                        showTimePickerDialog = !showTimePickerDialog
                    },
                    onAccept = { minutes ->
                        //todo set here the timer
                        pickedTime = (minutes * 60 * 1000).toLong()
                        showTimePickerDialog = !showTimePickerDialog
                        isTimePicked = !isTimePicked
                    })
            }
        }
    }
    BackHandler(enabled = true, onBack = {
        showPlayerFullScreen = false
        navController.popBackStack()
    })
}