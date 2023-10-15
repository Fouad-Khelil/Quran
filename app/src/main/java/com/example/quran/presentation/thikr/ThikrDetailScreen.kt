package com.example.quran.presentation.thikr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quran.R
import com.example.quran.others.isItInDarkTheme
import com.example.quran.presentation.otherApps.AboutTheApp.shareSomething
import com.example.quran.ui.theme.noto_arabic_font
import com.example.quran.ui.theme.onSecondaryBackground
import com.example.quran.ui.theme.secondaryBackgroundColor

@Composable
fun ThikrDetailScreen(
    category: String,
    thikrDetailViewModel: ThikrDetailViewModel = hiltViewModel()
) {

    val allThikrByCategory by thikrDetailViewModel.allThikrByCategory

    LaunchedEffect(key1 = category) {
        thikrDetailViewModel.getAllSurahs(category)
    }


    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(9.dp))
        }
        items(allThikrByCategory) { thikr ->
            ThikrDetailItem(
                thikr.zekr,
                thikr.description,
                thikr.count
            )
        }
    }
}


@Composable
fun ThikrDetailItem(thikrText: String, description: String, numberOfRepeat: String) {

    var visible by remember { mutableStateOf(false) }
    val context =  LocalContext.current
    val shareLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 9.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colors.surface)
//            .clickable {
//            }
            .padding(12.dp)


    ) {
        Text(
            modifier = Modifier.clickable {
                visible = !visible

            },
            text = thikrText, fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(visible = visible) {
            Column {
                Divider(color = onSecondaryBackground)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontFamily = noto_arabic_font,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary)
                            .padding(horizontal = 18.dp)
                            .align(Alignment.Top),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = if (numberOfRepeat.isEmpty()) "غير محدد" else numberOfRepeat + " مرة ",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        modifier = Modifier.clickable {
                            copyText(thikrText , context)
                        },
                        painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                        contentDescription = "",
                        tint = if(isItInDarkTheme()) secondaryBackgroundColor else onSecondaryBackground
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        modifier = Modifier.clickable {
                            shareSomething(thikrText+"\n"+description, context , shareLauncher)
                        },
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "",
                        tint = if(isItInDarkTheme()) secondaryBackgroundColor else onSecondaryBackground
                    )
                }
            }
        }

    }
}

fun copyText(text: String, context: Context) {
    val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("copied text", text)
    clipBoard.setPrimaryClip(clip)
}


@Preview(showBackground = true)
@Composable
fun ThikrDetailItemPreview() {
    ThikrDetailItem(
        "اللّهـمَّ أَنْتَ رَبِّـي لا إلهَ إلاّ أَنْتَ  خَلَقْتَنـي وَأَنا عَبْـدُك  وَأَنا عَلـى عَهْـدِكَ وَوَعْـدِكَ ما اسْتَـطَعْـت  أَعـوذُبِكَ مِنْ شَـرِّ ما صَنَـعْت  أَبـوءُ لَـكَ بِنِعْـمَتِـكَ عَلَـيَّ وَأَبـوءُ بِذَنْـبي فَاغْفـِرْ لي فَإِنَّـهُ لا يَغْـفِرُ الذُّنـوبَ إِلاّ أَنْتَ",
        "من قالها موقنا بها حين يمسي  مات من ليلته دخل الجنة و كذلك حين يصبح",
        "1"
    )
}