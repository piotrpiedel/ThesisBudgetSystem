package piedel.piotr.thesis.util

import java.text.SimpleDateFormat
import java.util.*

fun simpleDateFormat(): SimpleDateFormat {
    val myFormat = "dd MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.ENGLISH)
}

fun simpleDateFormatFromJson(): SimpleDateFormat {
    val myFormat = "yyyy-MM-dd"
    return SimpleDateFormat(myFormat, Locale.ENGLISH)
}

fun simpleDateFormatInStringFormat(): String {
    return "dd MMMM yyyy"
}

fun dateFromString(value: String): Date? {
    return if (value.isNotBlank()) {
        simpleDateFormat().parse(value)
    } else {
        null
    }
}

fun dateFromStringNullCheck(value: String): Date {
    return if (value.isNotBlank()) {
        simpleDateFormat().parse(value)
    } else return Date()
}

fun stringFormatDate(date: Date?): String? {
    date?.let { return simpleDateFormat().format(date) } ?: return null
}

fun dateToTextString(date: Date?): String? {
    date?.let { return simpleDateFormat().format(date) } ?: return null
}