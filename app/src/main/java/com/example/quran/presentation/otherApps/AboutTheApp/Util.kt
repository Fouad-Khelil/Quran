package com.example.quran.presentation.otherApps.AboutTheApp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat.startActivity
import com.example.quran.others.Constants


fun openPlayStoreForRating(context: Context) {
    val uri = Uri.parse("market://details?id=${Constants.PACKAGE_NAME}")
    val goToMarketIntent = Intent(Intent.ACTION_VIEW, uri)

    // If the Play Store app is not available, open the Play Store website
    try {
        context.startActivity(goToMarketIntent)
    } catch (e: ActivityNotFoundException) {
        val webUri =
            Uri.parse("https://play.google.com/store/apps/details?id=${Constants.PACKAGE_NAME}")
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        context.startActivity(webIntent)
    }
}

fun redirectToGmail(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:application.zed@gmail.com")
    }
    context.startActivity(intent)
}

fun shareSomething(
    postContent: String,
    context: Context,
    shareLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, postContent)

    val chooserIntent = Intent.createChooser(intent, "Share post via")

    if (intent.resolveActivity(context.packageManager) != null) {
        shareLauncher.launch(chooserIntent)
    }
}


fun openLink(context: Context, link: String, openLinkLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    val packageManager = context.packageManager
    val resolvedActivity = intent.resolveActivity(packageManager)

    if (resolvedActivity != null) {
        // There is an app to handle the link, so open it within the app
        startActivity(context, intent, null)
    } else {
        // No app to handle the link, so open it in a browser
        openLinkLauncher.launch(intent)
    }
}