package piedel.piotr.thesis.util

import java.text.SimpleDateFormat
import java.util.*

fun simpleDateFormat(): SimpleDateFormat {
    val myFormat = "dd MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.ENGLISH)
}

fun dateFromString(value: String): Date? {
    return if (value.isNotBlank()) {
        simpleDateFormat().parse(value)
    } else {
        null
    }
}

fun stringFormatDate(date: Date?): String? {
    date?.let { return simpleDateFormat().format(date) } ?: return null
}