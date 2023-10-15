package com.example.quran.presentation.readableQuran.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.navigation.Screen
import com.example.quran.others.AyahPrettyTextTransformer
import com.example.quran.others.Constants
import com.example.quran.ui.theme.hafs_uthmanic_font
import com.example.quran.ui.theme.noto_arabic_font
import com.example.quran.ui.theme.secondaryBackgroundColor
import java.text.MessageFormat


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AyahSearchScreen(
    navController: NavController ,
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val searchResult = searchViewModel.ayahsSearchResult.collectAsState().value
    val isSearchPerformed = searchViewModel.isSearchPerformed.collectAsState().value
    Scaffold (topBar = {
        SearchHeader(navController = navController, numAyahs = searchResult.size, onSearch = {
            searchViewModel.search(it)
        })
    }){
        LazyColumn(
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 8.dp,
                bottom = 56.dp
            )
        ) {
            items(searchResult) { ayah ->
                SearchResultItem(ayahNumber = ayah.num.toInt(), surah =ayah.surahName ,ayah= " ${ayah.text}"){
                    searchViewModel.getPageFromSurahNumAndAyahNum(ayah.surahNum.toInt(),ayah.num.toInt()){page->
                        Log.d("page_result", "page:$page ")
                        navController.navigate(Screen.QuranPaperScreen.pageRoute(Constants.NAVIGATE_FROM_SEARCH , page))
                    }
                }
            }
        }

        if (isSearchPerformed && searchResult.isEmpty()) {
            NoResultItem(title = "لا توجد  نتائج", description = "تأكد من كتابتك  للاية كتابة صحيحة", image = R.drawable.ic_no_result)
        }

        if (!isSearchPerformed) {
            NoResultItem(title = "ابحث عن اية", description = "تأكد من كتابتك  للاية كتابة صحيحة", image = R.drawable.ic_book)
        }
    }
}


@Composable
private fun SearchHeader(
    navController: NavController,
    numAyahs: Int = 0,
    onSearch: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var searchedAyah by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp) , modifier = Modifier.padding(18.dp, 18.dp,18.dp, 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "البحث", fontSize = 24.sp, fontFamily = noto_arabic_font)
        }

        TextField(
            value = searchedAyah,
            onValueChange = { ayah ->
                searchedAyah = ayah
            },
            placeholder = {
                Text(
                    stringResource(id = R.string.search_for_ayah),
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
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    if (searchedAyah.isNotEmpty() &&searchedAyah.length >1){
                        onSearch(searchedAyah)
                    }
                }
            ),
        )
        if (numAyahs > 0) {
            Text(text = " عدد الايات : $numAyahs " , fontFamily = noto_arabic_font , fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp, start = 4.dp))
        }
    }
}


@Composable
fun NoResultItem(title : String , description : String , @DrawableRes image :Int) {
    Column (Modifier.fillMaxSize() , horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center){
        Image(painter = painterResource(id = image), contentDescription = null)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title ,
            fontSize = 22.sp,
            fontFamily = noto_arabic_font ,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            fontFamily = noto_arabic_font
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchResultItem(ayahNumber: Int ,surah :String, ayah: String , onClick:()  ->Unit) {
    val ayahNumberString =
        MessageFormat.format("{0}", AyahPrettyTextTransformer.getArabicNumber(ayahNumber))
    Card(
        elevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(vertical = 8.dp) ,
        onClick = {
            onClick()
        }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = 28.sp ,
                            fontFamily = hafs_uthmanic_font
                        )
                    ) {
                        append(ayahNumberString)
                    }
                    append("  " + surah)
                },
                 fontSize = 16.sp ,
                fontFamily = noto_arabic_font
            )

            Text(
                text =ayah,
                fontSize = 13.sp,
                fontFamily = noto_arabic_font ,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp)
            )

        }
    }
}