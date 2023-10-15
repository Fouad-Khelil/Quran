package com.example.quran.presentation.auditableQuran

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.quran.R
import com.example.quran.others.isItInDarkTheme
import com.example.quran.ui.theme.complimentaryColor
import com.example.quran.ui.theme.onDarkBackground
import com.example.quran.ui.theme.onSecondaryBackground
import com.example.quran.ui.theme.onSecondaryDarkBackground
import com.example.quran.ui.theme.primaryColor
import com.example.quran.ui.theme.secondaryBackgroundColor

@Composable
fun ReciterNormalModeItem(
    modifier: Modifier,
    reciterName: String,
    rewayah: String,
    reciterPhoto: String,
    isAddedToFavorite: Boolean,
    onClickFavorite: () -> Unit,
    onClickReciterItem: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickReciterItem()
            }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            ReciterImage(imageUrl = reciterPhoto)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = reciterName,
                    fontSize = 18.sp,
                )
                Text(
                    text = rewayah,
                    fontSize = 14.sp,
                    color = if (isItInDarkTheme()) onSecondaryDarkBackground else onSecondaryBackground
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        Column {
            Spacer(modifier = Modifier.height(4.dp))
            IconButton(
                onClick = {
                    onClickFavorite()
                },
            ) {
                Icon(
                    painter = painterResource(id = if (!isAddedToFavorite) R.drawable.bookmark_border else R.drawable.bookmark),
                    contentDescription = "",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun ReciterBinaryModeItem(
    reciterPhoto: String,
    reciterName: String,
    onClickReciterDualMode: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onClickReciterDualMode()
        }
    ) {
        AsyncImage(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.4f),
                    shape = CircleShape
                ),
            model = ImageRequest.Builder(LocalContext.current)
                .data(reciterPhoto)
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.img_error),
            error = painterResource(id = R.drawable.img_error),
        )



        Spacer(modifier = Modifier.height(8.dp))
        Text(text = reciterName, fontSize = 14.sp)
    }

}


@Composable
fun AudioControleBar(
    isPlay: Boolean = false,
    onPlay: () -> Unit,
    onSkipForward: () -> Unit,
    onSkipPrevious: () -> Unit,
    onReplayFiveForward: () -> Unit,
    onReplayFiveBackward: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
    ) {
        Icon(
            painterResource(id = R.drawable.ic_next_play_),
            contentDescription = "Forward 10 seconds",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onSkipPrevious)
                .padding(8.dp)
                .size(20.dp),
            tint = onSecondaryBackground
        )
        Spacer(modifier = Modifier.width(18.dp))
        Icon(
            painterResource(id = R.drawable.ic_forward_5_seconds),
            contentDescription = "Skip Next",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onReplayFiveBackward)
                .padding(8.dp)
                .size(28.dp),
            tint = onSecondaryBackground
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(painter = painterResource(if (!isPlay) R.drawable.ic_round_play_circle_24 else R.drawable.ic_round_pause_circle_24),
            contentDescription = "Play",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onPlay()
                }
                .size(64.dp)

        )
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painterResource(id = R.drawable.back_5_seconds),
            contentDescription = "Replay 10 seconds",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onReplayFiveForward)
                .padding(8.dp)
                .size(28.dp),
            tint = onSecondaryBackground
        )
        Spacer(modifier = Modifier.width(18.dp))

        Icon(
            painterResource(id = R.drawable.previous_play),
            contentDescription = "Skip Previous",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onSkipForward)
                .padding(8.dp)
                .size(20.dp),
            tint = onSecondaryBackground
        )
    }
}

@Composable
fun SliderItem(
    playbackProgress: Float,
    currentTime: String,
    totalTime: String,
    onSliderChange: (Float) -> Unit,
    onSliderChangeFinished: () -> Unit,

    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        Slider(
            value = playbackProgress,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.primary,
                activeTrackColor = MaterialTheme.colors.primary,
                inactiveTrackColor = secondaryBackgroundColor
            ),
            onValueChange = onSliderChange,
            onValueChangeFinished = onSliderChangeFinished
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                currentTime,
                style = MaterialTheme.typography.body2.copy(),
                color = MaterialTheme.colors.primary.copy(alpha = 0.6f)
            )
            Text(
                totalTime,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun RepeatControllerBar(
    isRepeatModeClicked: Boolean = false,
    onTimePickerClick: () -> Unit,
    onRepeatSurah: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
//        Button(
//            onClick = { /*TODO*/ },
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = MaterialTheme.colors.primary,
//                contentColor = Color.White
//            ),
//            shape = CircleShape,
//            elevation = ButtonDefaults.elevation(0.dp)
//        ) {
//            Text(
//                "تشغيل الكل",
//                fontSize = 16.sp
//            )
//        }
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.ic_timer),
            contentDescription = "timer",
            tint = onSecondaryBackground,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onTimePickerClick()
                }
        )
        Spacer(modifier = Modifier.width(24.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_infinite_repeat),
            contentDescription = "infinite repeat",
            tint = if (isRepeatModeClicked) primaryColor else onSecondaryBackground,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onRepeatSurah()
                }
        )
//        }

    }
}

@Composable
fun AudioTopBar(
    onBackIconClick: () -> Unit,
    onDownloadIconClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        IconButton(onClick = { onBackIconClick() }) {
            Icon(
                modifier = Modifier
                    .rotate(-90f),
                painter = painterResource(id = R.drawable.ic_round_navigate_next_24),
                contentDescription = "",
                tint = onSecondaryBackground,
            )
        }
    }
}

@Composable
fun CollapseRunningSora(
    modifier: Modifier = Modifier,
    soraName: String,
    reciterName: String,
    isPlay: Boolean = false,
    onClickBottomPlayBarPlayIcon: () -> Unit,
    onClear: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.background(MaterialTheme.colors.primary)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onClear()
                    },
                painter = painterResource(id = R.drawable.ic_round_clear_24),
                contentDescription = "",
                tint = Color.White
            )
            Text(text = "$soraName - $reciterName", fontSize = 18.sp, color = Color.White)
        }

        Icon(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    onClickBottomPlayBarPlayIcon()
                },
            painter = painterResource(id = if (!isPlay) R.drawable.ic_round_pause_24 else R.drawable.ic_play),
            contentDescription = "",
            tint = Color.White
        )
    }

}

@Composable
fun TimePickDialog(
    isPicked: Boolean = false,
    countDownTime: String,
    onDismiss: () -> Unit,
    onAccept: (Int) -> Unit
) {
    val context = LocalContext.current
    var pickerValue by remember { mutableStateOf(5) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.width(280.dp),
//            modifier = Modifier.padding(8.dp),
            elevation = 8.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "اختر مدة التلاوة",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )

                if (!isPicked) {
                    NumberPicker(
                        modifier = Modifier.width(200.dp),
                        value = pickerValue,
                        range = 5..120 step 5,
                        onValueChange = {
                            pickerValue = it
                        },
                        textStyle = TextStyle(fontSize = 18.sp)
                    )
                } else {
                    Text(text = countDownTime, fontSize = 64.sp, modifier = Modifier.padding(24.dp))
                }

                Row {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isPicked) complimentaryColor else primaryColor
                        ),
                        onClick = {
                            Toast.makeText(context, "تم تفعيل الموقت", Toast.LENGTH_SHORT).show()
                            onAccept(pickerValue)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 4.dp, bottom = 2.dp)
                            .weight(1F),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Text(text = if (!isPicked) "موافق" else "ايقاف")
                    }

                    OutlinedButton(
                        onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 8.dp, bottom = 2.dp)
                            .weight(1F)
                    ) {
                        Text(text = "الغاء")
                    }

                }
            }
        }
    }
}

@Composable
fun ReciterImage(
    imageUrl: String?,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = Modifier.size(56.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            // Display a progress bar while loading
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(36.dp),
                color = MaterialTheme.colors.primary,
            )
        }

        Image(
            modifier = Modifier
                .height(56.dp)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop,
            painter = if (isError.not() && !isLocalInspection) {
                imageLoader
            } else {
                painterResource(R.drawable.img_error)
            },
            // TODO b/226661685: Investigate using alt text of  image to populate content description
            contentDescription = null, // decorative image,
        )
    }
}


//-------------preview ---------
@Preview
@Composable
fun SliderItemPreview() {
    SliderItem(
        playbackProgress = 0.5f,
        currentTime = "1:00",
        totalTime = "5:00",
        onSliderChange = {}) {}
}


@Preview(showBackground = true)
@Composable
fun CollapseRunningSoraPreview() {
//    CollapseRunningSora(soraName = "الاعراف", reciterName = "الغامدي") {}
}

@Preview
@Composable
fun Reciter() {
//    ReciterNormalModeItem(
//        "سعد الغامدي", "رواية حفص", R.drawable.mokri
//    ) {}
}