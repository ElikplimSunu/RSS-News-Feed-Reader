package com.sunueric.newsrssfeedapp.utils

import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale


fun calculateTimeAgo(past: ZonedDateTime): String {
    val now = ZonedDateTime.now()
    val duration = Duration.between(past, now)

    return when {
        duration.toMillis() == 1000L -> "just now"
        duration.toMillis() < 60000L -> "${duration.toMillis()/1000} seconds ago"
        duration.toMinutes() == 1L -> "a minute ago"
        duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
        duration.toHours() == 1L -> "an hour ago"
        duration.toHours() < 24 -> "${duration.toHours()} hours ago"
        duration.toDays() == 1L -> "a day ago"
        else -> "${duration.toDays()} days ago"
    }
}

fun parseDateTime(dateTimeStr: String): ZonedDateTime {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.getDefault())
    return ZonedDateTime.parse(dateTimeStr, formatter)
}