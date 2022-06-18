package com.policlinsaude.newfeature.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent


fun Context.openBrowser(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(
        this,
        Uri.parse(url)
    )
}