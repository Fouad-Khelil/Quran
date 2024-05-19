package com.example.quran.presentation.auditableQuran

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.exoplayer.Sora
import com.example.quran.exoplayer.isPlaying
import com.example.quran.exoplayer.toSora
import com.example.quran.navigation.Screen
import com.example.quran.others.Resource
import com.example.quran.others.formatLink
import com.example.quran.presentation.auditableQuran.viewmodels.AllSoraAudioViewModel
import com.example.quran.presentation.readableQuran.AllSoraScreenViewModel
import com.example.quran.presentation.sharedUi.AllSoraScreen
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AllSoraAudioScreen(
    navController: NavController,
    collapsingRunningSoraPadding: Dp = 56.dp,
    reciterName: String = "",
    rewayah: String = "",
    photo: String = "",
    server: String = "",
    surahList: String = "",
    allSoraScreenViewModel: AllSoraScreenViewModel = hiltViewModel(),
    allSoraAudioViewModel: AllSoraAudioViewModel = hiltViewModel()
) {
    var isCollapsingStateCleared by remember { mutableStateOf(false) }
    val currentSora = allSoraAudioViewModel.currentPlayingSora.value
    var mediaItm by remember { mutableStateOf(Sora(mediaId = reciterName + "1")) }
    val playbackStateCompat = allSoraAudioViewModel.playbackState.value
    var offsetX by remember { mutableStateOf(0f) }
    var searchedSurah by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        allSoraAudioViewModel.init(reciterName)
    }

    val allSurahs by allSoraScreenViewModel.allSurahs

    LaunchedEffect(key1 = allSurahs) {
        allSoraScreenViewModel.getReciterAvailableSurahs(surahList)
    }

    Box(contentAlignment = Alignment.BottomCenter) {
        val sora = Sora("", reciterName, "", server, photo)
        AllSoraScreen(
            sora = "",
            hizbOrMokri = DataStoreRepository(LocalContext.current).getLastReciter().name,
            icon = R.drawable.ic_play_outlined,
            isAudio = true,
            surahs =allSurahs ,
            onSearchSurah = { surah ->
                searchedSurah = surah
                allSoraScreenViewModel.getSearchedSurahs(surah)

            },
            onClickLastReading = {
                val lastListening =allSoraScreenViewModel.getLastListeningSurah()
                allSoraAudioViewModel.playOrToggleSora(Sora(mediaId = reciterName+lastListening))
                navController.navigate(Screen.AudioPlayerScreen.createRoute(rewayah))
            },
            onClickPlayIcon = { soraItem ->
                val mediaId = reciterName + soraItem.soraOrder.toString()
                mediaItm = Sora(
                    name = soraItem.soraName,
                    reciter = sora.reciter,
                    mediaId = mediaId,
                    soraUrl = formatLink(soraItem.soraOrder, sora.soraUrl),
                    imageUrl = sora.imageUrl
                )
                if (isCollapsingStateCleared) {
                    isCollapsingStateCleared = !isCollapsingStateCleared
                }
                allSoraAudioViewModel.playOrToggleSora(mediaItm, true)
                allSoraAudioViewModel.showPlayerFullScreen = true
            },
            searchedSurah =searchedSurah ,
            onClickSora = {
                allSoraAudioViewModel.playOrToggleSora(mediaItm.copy(mediaId = reciterName + it), true)
                navController.navigate(Screen.AudioPlayerScreen.createRoute(rewayah))
            }
        )

        AnimatedVisibility(!isCollapsingStateCleared) {
            if (currentSora != null) {
                val sora = currentSora.toSora()
                CollapseRunningSora(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = collapsingRunningSoraPadding)
                        .height(56.dp)
                        .padding(horizontal = 8.dp)
                        .clip(MaterialTheme.shapes.small)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    when {
                                        offsetX > 0 -> {
                                            allSoraAudioViewModel.skipToPreviousSora()
                                        }

                                        offsetX < 0 -> {
                                            allSoraAudioViewModel.skipToNextSora()
                                        }
                                    }
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val (x, y) = dragAmount
                                    offsetX = x
                                }
                            )

                        },
                    soraName = sora!!.name,
                    reciterName = sora.reciter,
                    isPlay = playbackStateCompat?.isPlaying == false,
                    onClear = {
                        isCollapsingStateCleared = !isCollapsingStateCleared
                    },
                    onClickBottomPlayBarPlayIcon = {
                        if (mediaItm.mediaId.isNotEmpty()) {
                            allSoraAudioViewModel.playOrToggleSora(
                                mediaItm.copy(mediaId = sora.mediaId),
                                true
                            )
                            allSoraAudioViewModel.showPlayerFullScreen = true
                        }
                    }
                )
            }
        }
    }
}