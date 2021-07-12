package com.dupat.newsqu.utils

import android.app.Activity
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import java.text.SimpleDateFormat
import java.util.*

fun Date.toTimeAgo() : String{
    val timeMilis = this.time
    val localByLanguageTag = Locale.forLanguageTag("en")
    val timeAgoMessage = TimeAgoMessages.Builder().withLocale(localByLanguageTag).build()
    return TimeAgo.using(timeMilis, timeAgoMessage)
}

fun Date.toTimeDetail() : String{
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy HH:mm")
    return dateFormat.format(this)
}

fun String.toLocalDate() : Date{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    return dateFormat.parse(this)
}

fun Activity.statusBarHeight(): Int{
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}