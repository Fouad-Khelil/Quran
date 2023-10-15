package com.example.quran.presentation.otherApps.AboutTheApp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quran.R
import com.example.quran.others.Constants
import com.example.quran.presentation.otherApps.settings.IconType
import com.example.quran.presentation.otherApps.settings.SettingItem
import com.example.quran.ui.theme.noto_arabic_font
import com.example.quran.ui.theme.onSecondaryBackground


@Composable
fun AboutTheAppScreen(navController: NavController) {
    val headerTextStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = noto_arabic_font,
        color = MaterialTheme.colors.primary
    )

    val openLinkLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    //the share launcher
    val shareLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppHeader(navController, R.string.about_the_app)

        Text(
            text = stringResource(id = R.string.app_description_title),
            style = headerTextStyle
        )

//        Card(
//            elevation = 0.dp,
//            shape = MaterialTheme.shapes.medium,
//            backgroundColor = secondaryBackgroundColor
//        ) {
        Text(
            text = stringResource(id = R.string.app_description),
            modifier = Modifier.padding(start = 4.dp),
            fontFamily = noto_arabic_font,
            textAlign = TextAlign.Justify ,
            color = onSecondaryBackground
        )
//        }
//        Spacer(modifier = Modifier.height(4.dp))
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        SettingItem(
            text = R.string.rate_the_app,
            icon = IconType.Vector(Icons.Outlined.Star),
        ) {
            openPlayStoreForRating(context)
        }

        SettingItem(
            text = R.string.share_the_app,
            icon = IconType.Vector(Icons.Outlined.Share),
        ) {
            val postContent = "تطبيق اسلامي يضم معظم ما يحتاجه المسلم.\n ${Constants.PACKAGE_NAME}"

            shareSomething(postContent,context, shareLauncher)
        }

        SettingItem(
            text = R.string.tech_support,
            icon = null,
        ) {
            redirectToGmail(context)
        }

        ////////////////social media section
        Text(
            text = stringResource(id = R.string.social_media),
            style = headerTextStyle
        )

        SettingItem(
            text = R.string.telegram,
            icon = IconType.Painterr(painterResource(id = R.drawable.telegram)),
            isTintApplied = false
        ) {
            openLink(context, Constants.TELEGRAM_LINK, openLinkLauncher)
        }

        SettingItem(
            text = R.string.facebook,
            icon = IconType.Painterr(painterResource(id = R.drawable.ic_facebook)),
            isTintApplied = false
        ) {
            openLink(context, Constants.FACEBOOK_LINK, openLinkLauncher)
        }

        SettingItem(
            text = R.string.instagram,
            icon = IconType.Painterr(painterResource(id = R.drawable.instagram)),
        ) {
            openLink(context, Constants.INSTAGRAM_LINK, openLinkLauncher)
        }

        Spacer(modifier = Modifier.height(2.dp))
        Divider()
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(id = R.string.app_version),
            fontFamily = noto_arabic_font,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

    }
}



@Composable
fun AppHeader(navController: NavController, @StringRes pageText :Int ) {
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
        Text(
            text = stringResource(id =pageText),
            fontSize = 24.sp,
            fontFamily = noto_arabic_font
        )
    }
}














