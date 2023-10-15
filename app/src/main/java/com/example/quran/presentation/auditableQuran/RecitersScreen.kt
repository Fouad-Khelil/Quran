package com.example.quran.presentation.auditableQuran

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.exoplayer.QuranService
import com.example.quran.navigation.Screen
import com.example.quran.others.Constants
import com.example.quran.others.Resource
import com.example.quran.presentation.TrackScrollJank
import com.example.quran.presentation.home.DecorationLastViewItem
import com.example.quran.presentation.sharedUi.ChipGroup
import com.example.quran.ui.theme.hafs_uthmanic_font
import com.example.quran.ui.theme.noto_arabic_font
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


val reciterFavoriteChips = listOf("  الكل  ", " المفضلة ")


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecitersScreen(
    navController: NavController,
    recitersViewModel: RecitersViewModel = hiltViewModel()
) {

    var searchedReciter by remember { mutableStateOf("") }
    var filterFavorite by remember { mutableStateOf(false) }
    var reciterId by remember { mutableStateOf(1) }



    LaunchedEffect(searchedReciter) {
        recitersViewModel.searchReciter(searchedReciter)
    }
    val context = LocalContext.current
    val intent = Intent(context, QuranService::class.java).apply {
        putExtra("reciter", reciterId)
    }

    LaunchedEffect(key1 = Unit){
        context.startService(intent)
    }

    val dataStore = DataStoreRepository(context)

    // produce state surround the value to state  and launch a coroutine (mutableStateOf + launchEffect)
    val reciterList = recitersViewModel.recitersList.collectAsState().value

    val state = rememberLazyListState()
    TrackScrollJank(scrollableState = state, stateName = "reciters:screen")

    LazyColumn(
//        modifier = Modifier.padding(18.dp),
        state = state,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 18.dp, start = 18.dp, end = 18.dp, bottom = 60.dp) ,
        modifier = Modifier.testTag("reciter_list")
    ) {
        item {
            DecorationLastViewItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                sora = dataStore.getLastListeningSurah().toString(),
                hizbOrMokri = dataStore.getLastReciter().name,
                lastAction = R.string.last_listening,
                icon = R.drawable.ic_play_outlined
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = searchedReciter,
                onValueChange = { reciter ->
                    searchedReciter = reciter
//                    recitersViewModel.searchReciter(searchedReciter)
                },
                placeholder = {
                    Text(
                        stringResource(id = R.string.search_for_reciter),
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
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.binary_mode_text),
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                itemsIndexed(Constants.DUAL_LIST) { index, item ->
                    ReciterBinaryModeItem(item.photo, item.reciter) {
                        navController.navigate(Screen.AudioDualScreen.createRoute(index))
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(id = R.string.normal_mode_text),
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))

            ChipGroup(
                chipContent = reciterFavoriteChips,
                PaddingValues(horizontal = 0.dp),
                onChipClick = { index ->
                    filterFavorite = index == 1
                    recitersViewModel.getAllReciters(filterFavorite)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

        }

        if (reciterList is Resource.Success) {
            if (filterFavorite && reciterList.data.isNullOrEmpty()) {
                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(36.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_no_result),
                            contentDescription = null,
                            modifier = Modifier.size(140.dp)
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "لم يتم اضافة قراء الى المفضلة",
                            fontSize = 14.sp,
                            fontFamily = noto_arabic_font,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            } else {
                items(
                    items = reciterList.data ?: emptyList(),
                    key = { it.id },
                    itemContent = { reciter ->
                        ReciterNormalModeItem(
                            modifier = Modifier.animateItemPlacement(),
                            reciter.name,
                            reciter.rewayah,
                            reciter.photo,
                            reciter.is_favorite,
                            onClickFavorite = {
                                recitersViewModel.toggleFavorite(reciter, filterFavorite)
                            },
                            onClickReciterItem = {
                                reciterId = reciter.id
                                val encodedUrlPhoto =
                                    URLEncoder.encode(
                                        reciter.photo,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                val encodedUrlServer =
                                    URLEncoder.encode(
                                        reciter.server,
                                        StandardCharsets.UTF_8.toString()
                                    )

                                recitersViewModel.saveLastReciter(reciter)

                                navController.navigate(
                                    Screen.AllSoraAudioScreen.createRoute(
                                        reciter.name,
                                        reciter.rewayah,
                                        encodedUrlPhoto,
                                        encodedUrlServer,
                                        reciter.surah_list
                                    )
                                )
                            }
                        )
                    }
                )

            }
        }

    }


}


//-----------------data ------------------------
data class ReciterItem(val name: String, val rewayah: String, val photo: String)


