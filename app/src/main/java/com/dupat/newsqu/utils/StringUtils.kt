package com.dupat.newsqu.utils

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

fun String.toLocalDate() : Date{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    return dateFormat.parse(this)
}