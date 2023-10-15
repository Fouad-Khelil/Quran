import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.batoulapps.adhan.PrayerTimes
import com.example.quran.R
import com.example.quran.ui.theme.complimentaryColor
import com.example.quran.ui.theme.hafs_uthmanic_font
import com.example.quran.ui.theme.primaryColor


val textStyle1 = TextStyle(
    fontSize = 20.sp,
    color = Color.White,
    fontFamily = hafs_uthmanic_font
)

val textStyle2 = TextStyle(
    fontSize = 24.sp,
    color = Color.White,
    fontFamily = hafs_uthmanic_font
)

@Composable
fun CardMain(
    nextSalat: String,
    salatTime: String,
    date: String,
    hijriDate: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .padding(top = 18.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large),
            painter = painterResource(id = R.drawable.main_background),
            contentScale = ContentScale.Crop,
            contentDescription = "background main"
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = hijriDate,
                    style = textStyle1,
                )

                Text(
                    text = date,
                    style = textStyle1,
                )
            }

            Text(
                text = stringResource(id = R.string.next_salat),
                style = textStyle2
            )

            Text(
                text = "${nextSalat}   ${salatTime}",
                style = textStyle2
            )
        }
    }
}


@Composable
fun PrayerTimeItem(
    @DrawableRes icon: Int,
    time: String,
    backgroundColor: Color,
    modifier: Modifier
) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .padding(vertical = 7.dp, horizontal = 12.dp)
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = Color.White,
            contentDescription = "time prayer icon"
        )
        Text(text = time, fontSize = 14.sp, color = Color.White, fontFamily = FontFamily.Default)
    }
}


@Composable
fun PrayerTimesRow(times: List<String>, prayerTimeIndex: Int = 2) {
    Column {

        Text(
            text = stringResource(id = R.string.prayer_times),
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground,
            fontFamily = hafs_uthmanic_font,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(top = 16.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            prayerTimes.forEachIndexed { index, prayerTime ->
                PrayerTimeItem(
                    icon = prayerTime.icon,
                    time = times[index],
                    backgroundColor = if (prayerTimeIndex == index + 1) complimentaryColor else primaryColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

}

@Composable
fun khatmaProgressIndicator(
    percentage: Int = 0
) {

    Column(Modifier.padding(horizontal = 18.dp)) {
        var enabled by remember { mutableStateOf(false) }
        val progress by animateFloatAsState(
            targetValue = if (enabled) percentage.toFloat() / 100 else 0f,
            animationSpec = tween(
                durationMillis = 350, 100, FastOutSlowInEasing
            )
        )
        LaunchedEffect(key1 = true) {
            enabled = true
        }

        Text(
            text = stringResource(id = R.string.khatma_progress_percentage) + "$percentage %",
            fontSize = 18.sp,
            fontFamily = hafs_uthmanic_font
        )
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            progress = progress,
            backgroundColor = MaterialTheme.colors.surface
        )
    }
}


@Composable
fun TodayItem(
    @DrawableRes icon: Int,
    todayItem: String,
    source: String,
    content: String,
    fontFamily: FontFamily = hafs_uthmanic_font,
    fontSize: TextUnit = 18.sp
) {
    Column(Modifier.padding(horizontal = 18.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    painter = painterResource(id = icon), contentDescription = "",
                    tint = MaterialTheme.colors.primary
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) { append(todayItem) }
                        append(" - " + source)
                    })
            }
            Text(
                text = stringResource(id = R.string.more),
                fontSize = 18.sp,
                color = MaterialTheme.colors.primary,
                fontFamily = hafs_uthmanic_font
            )
        }

        Text(
            text = content,
            fontSize = fontSize,
            fontFamily = fontFamily
        )
        Spacer(modifier = Modifier.height(12.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = MaterialTheme.colors.surface
        )
    }
}


//----------preview

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun CardMainPreview() {
//    CardMain(nextSalat = "الفجر", salatTime = "6:55")
}


@Preview(showBackground = true)
@Composable
fun PrayerTimesPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
//        PrayerTimesRow()
    }
}


/*---------------data--------------*/

data class PrayerTime(val icon: Int, val time: String, val backgroundColor: Color)

val prayerTimes = listOf(
    PrayerTime(
        icon = R.drawable.salat_fadjr,
        time = "00:00",
        backgroundColor = Color(0xff2ED18E)
    ),
    PrayerTime(
        icon = R.drawable.salat_zhuhur,
        time = "00:00",
        backgroundColor = Color(0xffD12E71)
    ),
    PrayerTime(
        icon = R.drawable.salat_asr,
        time = "00:00",
        backgroundColor = Color(0xff2ED18E)
    ),
    PrayerTime(
        icon = R.drawable.salat_maghrib,
        time = "00:00",
        backgroundColor = Color(0xff2ED18E)
    ),
    PrayerTime(
        icon = R.drawable.salat_isha,
        time = "00:00",
        backgroundColor = Color(0xff2ED18E)
    )
)