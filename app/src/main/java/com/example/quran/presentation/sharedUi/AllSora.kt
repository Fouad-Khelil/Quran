package com.example.quran.presentation.sharedUi


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quran.R
import com.example.quran.presentation.TrackScrollJank
import com.example.quran.presentation.home.DecorationLastViewItem
import com.example.quran.presentation.readableQuran.SoraItem
import com.example.quran.ui.theme.hafs_uthmanic_font

@Composable
fun AllSoraScreen(
    sora: String = "البقرة",
    hizbOrMokri: String = "الحزب الستون",
    lastAction: Int = R.string.last_reading,
    icon: Int = R.drawable.ic_book_main,
    isAudio: Boolean = false,
    surahs: List<SoraItm>,
    onSearchSurah: (String) -> Unit,
    searchedSurah : String ,
    onClickLastReading: () -> Unit,
    onClickPlayIcon: (SoraItm) -> Unit,
    onClickSora: (Int) -> Unit
) {
    var searchedSora by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val state = rememberLazyListState()
    TrackScrollJank(scrollableState = state, stateName = "surahs:screen")

    LazyColumn(
        state = state,
        contentPadding = PaddingValues(bottom = 56.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        item {
            AllSurahsHeader(
                onClickLastReading,
                sora,
                hizbOrMokri,
                lastAction,
                icon,
                onSearchSurah,
                searchedSurah = searchedSurah,
                focusManager
            )
        }

        items(
            items = surahs, key = { it.soraOrder },
            itemContent = { sora ->
                SoraItem(
                    sora.soraOrder,
                    sora.soraName,
                    sora.makiOrMadani,
                    sora.numberOfAya,
                    "",
                    icon = if (isAudio) {
                        R.drawable.ic_play
                    } else R.drawable.ic_round_navigate_next_24,
                    onClickPlayIcon = {
                        if (isAudio) {
                            onClickPlayIcon(sora)
                        } else {
                            onClickSora(sora.soraOrder)
                        }
                    }
                ) {
                    onClickSora(sora.soraOrder)
                }
            })
    }

}

@Composable
private fun AllSurahsHeader(
    onClickLastReading: () -> Unit,
    sora: String,
    hizbOrMokri: String,
    lastAction: Int,
    icon: Int,
    onSearchSurah: (String) -> Unit,
    searchedSurah :String ,
    focusManager: FocusManager
) {
    Column {
        DecorationLastViewItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable {
                    onClickLastReading()
                },
            sora = sora,
            hizbOrMokri = hizbOrMokri,
            lastAction = lastAction,
            icon = icon
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            //                modifier = Modifier.focusRequester(focusRequester),
            value = searchedSurah,
            onValueChange = { sora ->
                onSearchSurah(sora)
            },
            placeholder = {
                Text(
                    stringResource(id = R.string.search_for_sora),
                    fontFamily = hafs_uthmanic_font,
                    fontSize = 18.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "search"
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onSearchSurah(searchedSurah)
                }
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

//--------------preview----------------------
@Preview(showBackground = true)
@Composable
fun AllSoraPreview() {
//    AllSoraScreen(){}
}


//-------------------data -------------------

@Immutable
data class SoraItm(
    val soraOrder: Int,
    val soraName: String,
    val makiOrMadani: String,
    val numberOfAya: Int,
    val lastReading: String
)

//val listOfSora = listOf(
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//    Sora(1, "البقرة", "مدنية", 286, "منذ 1 شهر"),
//)

