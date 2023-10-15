@file:Suppress("DEPRECATION")

package com.example.quran.presentation.auditableQuran.dual_mode

import android.annotation.SuppressLint
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quran.R
import com.example.quran.others.Constants
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SoraAudioDualMode(
    navController: NavController,
    reciterIndex: Int,
    viewModel: ExoPlayerViewModel = hiltViewModel()
) {

    var isControleLayersVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val alpha = remember { Animatable(initialValue = 1f) }

    val context = LocalContext.current

    val exoPlayer = viewModel.exoPlayer
    val concatenatingMediaSource = viewModel.concatenatingMediaSource

    val playButtonSize =
        animateDpAsState(targetValue = if (isControleLayersVisible) 64.dp else 48.dp)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        // Define a list of audio URLs
        val audioUrls = generateUrls(reciterIndex)

        // Create MediaSource for each URL and add it to ConcatenatingMediaSource
        audioUrls.forEach { url ->
            val mediaItem = MediaItem.fromUri(url)
            val mediaSource = ProgressiveMediaSource.Factory(
                DefaultDataSourceFactory(
                    context,
                    Util.getUserAgent(context, "QuranApp")
                )
            ).createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        // Set media source to the player
        exoPlayer.setMediaSource(concatenatingMediaSource)

        // Prepare the player
        exoPlayer.prepare()
    }

    LaunchedEffect(viewModel.currentTrackIndex.value) {
        alpha.animateTo(0f, animationSpec = tween(durationMillis = 200))
        viewModel.setCurrentTrack(reciterIndex)
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 800))
    }


    // Play when the screen is visible
    DisposableEffect(Unit) {
        exoPlayer.play()
        onDispose {
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
        }
    }


    Box(contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    isControleLayersVisible = !isControleLayersVisible
                    if (isControleLayersVisible) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                },
            painter = painterResource(id = R.drawable.img_nature_background),
            contentDescription = "",
            contentScale = ContentScale.FillHeight
        )


        ProfileInfo(modifier = Modifier.align(Alignment.TopStart), reciterIndex)

        AnimatedVisibility(
            visible = !isControleLayersVisible,
            enter = fadeIn(animationSpec = tween(150, easing = FastOutSlowInEasing)),
            exit = fadeOut(animationSpec = tween(150, easing = FastOutLinearInEasing)),
        ) {
            Text(
                text = viewModel.currentAyah.value,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(alpha.value) // Fades in/out
                    .padding(18.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.White)
                    .padding(vertical = 4.dp, horizontal = 8.dp) ,
                color = Color.Black
            )
        }


        AnimatedVisibility(
            visible = isControleLayersVisible,
            enter = fadeIn(animationSpec = tween(150, easing = FastOutLinearInEasing)),
            exit = fadeOut(animationSpec = tween(150, easing = FastOutSlowInEasing)),
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(playButtonSize.value)
                    .background(Color.DarkGray.copy(alpha = 0.6f))

            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ProfileInfo(modifier: Modifier, reciterIndex: Int) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(18.dp)
            .then(modifier),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically

        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        shape = CircleShape,
                        color = MaterialTheme.colors.primary
                    ),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Constants.DUAL_LIST[reciterIndex].photo)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.FillHeight
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = Constants.DUAL_LIST[reciterIndex].reciter,
                color = Color.White
            )

        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "سورة " + Constants.DUAL_LIST[reciterIndex].surah,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.primary)
                .padding(horizontal = 8.dp, vertical = 2.dp),
            color = Color.White
//            fontsize
        )
    }

}


fun generateUrls(reciterIndex: Int) =
    (1..Constants.DUAL_LIST[reciterIndex].numberOfAyahs).map {
        Constants.DUAL_LIST[reciterIndex].server + Constants.DUAL_LIST[reciterIndex].surahIndex.toString()
            .padStart(3, '0') + it.toString().padStart(3, '0') + ".mp3"
    }

