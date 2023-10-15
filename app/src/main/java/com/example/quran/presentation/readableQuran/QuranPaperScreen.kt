package com.example.quran.presentation.readableQuran

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.navigation.Screen
import com.example.quran.others.Constants
import com.example.quran.others.colorMatrixWithBrightness
import com.example.quran.others.isItInDarkTheme
import com.example.quran.ui.theme.hafs_uthmanic_font
import com.example.quran.ui.theme.onDarkBackground
import com.example.quran.ui.theme.onSecondaryBackground
import com.example.quran.ui.theme.primaryColor
import com.example.quran.ui.theme.secondaryBackgroundColor
import com.example.quran.ui.theme.secondaryDarkBackground
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun QuranPaperWithBottomCheetScreen(
    surahIndex: Int = 1,
    page: Int = 1,
    navController: NavController,
    onThemeUpdated: (Int) -> Unit,
    quranPaperViewModel: QuranPaperViewModel = hiltViewModel()
) {
    val sheetstate = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetstate)

    val pageofAyahs = quranPaperViewModel.pageAyahs
    val currentPage = quranPaperViewModel.currentPage
    val currentSurah = quranPaperViewModel.currentSurah.collectAsState().value
    val tafsirSurah = quranPaperViewModel.tafsirSurahName.collectAsState().value


    val tafseerList = quranPaperViewModel.quranPageTafseerList
    var currentTafseerIndex by remember { mutableStateOf(0) }
    var bottomSheetContentState by rememberSaveable { mutableStateOf(QuranBottomSheetState.TAFSIR) }

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val isWorkDone = quranPaperViewModel.isCurrentPageReady.value

    val context = LocalContext.current

    var fontSize by remember { mutableStateOf(DataStoreRepository(context).getFontSize()) }
    var brightness by remember { mutableStateOf(0.5f) }

    val window = context.findActivity()?.window
    val params = window?.attributes

    val currentBrightness = params?.screenBrightness ?: 0.5f

    // Create a ValueAnimator to smoothly transition brightness
    val animator = rememberUpdatedState(ValueAnimator.ofFloat(currentBrightness, brightness).apply {
        duration = 300 // Adjust the duration as needed
        interpolator = LinearInterpolator()
        addUpdateListener { animation ->
            val newBrightness = animation.animatedValue as Float
            params?.screenBrightness = newBrightness
            window?.attributes = params
        }
    })

    // Update brightness when it changes
    LaunchedEffect(brightness) {
        this.launch { animator.value.start() }

        params?.screenBrightness = brightness
        window?.attributes = params

    }


    LaunchedEffect(Unit) {
        if (surahIndex == Constants.NAVIGATE_FROM_LAST_READING) {
            Log.d("scroll", "times")
            quranPaperViewModel.getLastReadingPage()
        } else if (surahIndex == Constants.NAVIGATE_FROM_SEARCH) {
            quranPaperViewModel.setCurrentPage(page)
        } else {
            Log.d("page_result", "paper screen page:reputed ")
            quranPaperViewModel.getSurahPage(surahIndex)
        }
    }


    coroutineScope.launch {
        if (currentPage.value != 0) {
            pagerState.scrollToPage(currentPage.value)
        }
    }


    LaunchedEffect(pagerState) {
        Log.d("paper", "surah index :$surahIndex")
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            quranPaperViewModel.getQuranPage(page)
            quranPaperViewModel.getSurahByPage(page)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            quranPaperViewModel.saveLastReadingPage(pagerState.currentPage) // save the last read page in the shared pref
            Log.d("scroll", "exit")
        }
    }


    BottomSheetScaffold(
        topBar = {
            if(DataStoreRepository(context).getAppBarSetting()){
                QuranTopBar(
                    navController,
                    currentSurah,
                    onClickSettings = {
                        bottomSheetContentState = QuranBottomSheetState.SETTINGS
                        coroutineScope.launch {
                            if (sheetstate.isCollapsed) {
                                sheetstate.expand()
                            } else {
                                sheetstate.collapse()
                            }
                        }
                    }
                )
                Divider(color = secondaryBackgroundColor)
            }
        },
        sheetBackgroundColor =if(isItInDarkTheme()) MaterialTheme.colors.surface else Color.White,
        sheetContent = {
            if (bottomSheetContentState == QuranBottomSheetState.SETTINGS) {
                SettingBottomSheet(
                    brightness = brightness,
                    fontSize = fontSize,
                    onChangeFontSize = {
                        Log.d("floatvalue", "the float value is :$it sp  : ${it.sp}")
                        fontSize = it
                        DataStoreRepository(context).setFontSize(it)
                    },
                    onChangeBrightness = {
                        brightness = it
                    },
                    onChangeTheme = onThemeUpdated
                )
            } else if (bottomSheetContentState == QuranBottomSheetState.TAFSIR) {
                if (tafseerList.value.isNotEmpty() && tafseerList.value.size > currentTafseerIndex) {
                    TafsirBottomCheet(
                        sora = tafsirSurah,
                        ayahRange = "الاية رقم ${tafseerList.value[currentTafseerIndex].numberInSurah}",
                        ayah = tafseerList.value[currentTafseerIndex].text,
                        tafsir = tafseerList.value[currentTafseerIndex].tafseer,
                        wordsExplanation = "لقد سبق القول في الحروف المقطعة",
                        navController
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "هنالك خطا حاول مرة اخرى")
                    }
                }

            }

        },
        sheetShape = MaterialTheme.shapes.medium,
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        modifier = Modifier.nestedScroll(connection = rememberNestedScrollInteropConnection())
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {

            HorizontalPager(count = Constants.QURAN_NUMBRE_OF_PAGES, state = pagerState) { page ->
                QuranPageScreen(
                    pageofAyahs.value, page,
                    onShowAyahTafseer = { ayahIndexInPage ->
                        bottomSheetContentState = QuranBottomSheetState.TAFSIR
                        currentTafseerIndex = ayahIndexInPage - 1
                        quranPaperViewModel.getSurahName(tafseerList.value[currentTafseerIndex].surahIndex)
                        coroutineScope.launch {
                            if (sheetstate.isCollapsed) {
                                sheetstate.expand()
                            } else {
                                sheetstate.collapse()
                            }
                        }
                    },
                    fontSize = fontSize.sp,
                )
            }
        }
    }
}

@Composable
private fun QuranTopBar(
    navController: NavController,
    surah: String,
    onClickSettings: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = null,
                tint = onSecondaryBackground
            )
        }


        Text(text = surah, fontSize = 20.sp)


        Row {
            IconButton(onClick = {
                navController.navigate(Screen.AyahSearchScreen.route)
            }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = onSecondaryBackground
                )
            }
            IconButton(onClick = {
                onClickSettings()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = onSecondaryBackground
                )
            }

        }

    }
}


@Composable
fun QuranPageScreen(
    pageofAyahs: String,
    page: Int = 1, // to implement later ,
    fontSize: TextUnit = 16.sp,
    onShowAyahTafseer: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        var ayahnumber by remember { mutableStateOf(1) }

        if (page < 2) {
            BasmalahDecorationItem(
                surah = "الاعراف",
                surahNumber = 2,
                numberOfAyahs = 5,
            )
        }

        QuranicText(
            fontSize = fontSize,
            quranicText = pageofAyahs,
            selectedAyah = ayahnumber, // Set this to the currently selected ayah, or null if none
            onAyahSelected = { ayahNumber ->
                // Handle the ayah selection event here
                ayahnumber = ayahNumber
                onShowAyahTafseer(ayahNumber)
            }
        )
    }
}

enum class QuranBottomSheetState {
    TAFSIR,
    SETTINGS
}


@Composable
fun QuranicText(
    quranicText: String,
    selectedAyah: Int?,
    onAyahSelected: (Int) -> Unit,
    fontSize: TextUnit = 16.sp
) {
    ClickableText(
        text = buildAnnotatedString {
            quranicText.split("\n").forEachIndexed { index, ayahText ->
                val ayahNumber = index + 1

                val number = ayahText.takeLastWhile { it.isDigit() || it.isWhitespace() }
                val ayah = ayahText.take(ayahText.length - number.length)

                withStyle(
                    SpanStyle(
                        fontSize = fontSize,
                        color = if (isItInDarkTheme()) Color.LightGray else Color.Black
                    )
                ) {
                    append(ayah)
                }

                withStyle(
                    SpanStyle(
                        fontSize = fontSize,
                        color = MaterialTheme.colors.primary
                    )
                ) {
                    append(number)
                }
            }
        },
        onClick = { offset ->
            // Determine which ayah was clicked based on the offset
            val clickedAyah = quranicText.substring(0, offset).count { it == '\n' } + 1
            onAyahSelected(clickedAyah)
        },
        style = TextStyle(
            fontFamily = hafs_uthmanic_font,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            textAlign = TextAlign.Justify
        ),
    )
}

@Composable
fun BasmalahDecorationItem(
    surah: String,
    surahNumber: Int,
    numberOfAyahs: Int,
    isBasmalahExist: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            painter = painterResource(id = R.drawable.img_decoration),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )
        Column(modifier = Modifier.padding(28.dp)) {
            Text(
                "سورة $surah",
                fontSize = 18.sp,
                fontFamily = hafs_uthmanic_font,
                color = onDarkBackground,
                modifier = Modifier.padding(start = 12.dp)
            )
//            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "ترتيبها " + surahNumber.toString(),
                    fontSize = 16.sp,
                    fontFamily = hafs_uthmanic_font,
                    color = onDarkBackground
                )
                Text(
                    "اياتها " + numberOfAyahs.toString(),
                    fontSize = 16.sp,
                    fontFamily = hafs_uthmanic_font,
                    color = onDarkBackground
                )
            }
//            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                modifier = Modifier
                    .width(300.dp)
                    .height(43.dp),
                painter = painterResource(id = R.drawable.ic_basmala),
                contentDescription = "",
                tint = onDarkBackground
            )
        }

    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}


//                ayahRange = "الاية رقم }",
//                ayah = "الٓمٓ (1) ذَٰلِكَ ٱلۡكِتَٰبُ لَا رَيۡبَۛ فِيهِۛ هُدٗى لِّلۡمُتَّقِينَ (2) ٱلَّذِينَ يُؤۡمِنُونَ بِٱلۡغَيۡبِ وَيُقِيمُونَ ٱلصَّلَوٰةَ وَمِمَّا رَزَقۡنَٰهُمۡ يُنفِقُونَ (3)",
//                tafsir = "لقد سبق القول في الحروف المقطعة",


//            if(pageofAyahs.value.isEmpty()) {
//                Text("page is empty")
//            }else {
//                Text(pageofAyahs.value)
//            }

