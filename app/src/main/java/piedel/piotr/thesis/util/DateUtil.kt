@file:Suppress("FunctionName", "LocalVariableName")

package piedel.piotr.thesis.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun String.stringAnyFormatToDefaultDateFormat(): Date? {
    return if (this.isNotBlank() && this.isNotEmpty()) {
        when {
            this.contains(regexDatePattern_YYYY_MM_DD) -> simpleDate_YYYY_MM_DD().parse(this)
            this.contains(regexDatePattern_DD_MM_YYYY) ->
                simpleDate_YYYY_MM_DD().parse( // parse return Date in desired pattern  yyyy-MM-DD
                        simpleDate_YYYY_MM_DD().format( // format return String in desired  pattern yyyy-MM-DD
                                simpleDateFormat_DD_MM_YYYY().parse(this))) // parse return Date in current
            else -> {
                Timber.e("DateUtil fun stringAnyFormatToDefaultDateFormat() NOT SUPPORTED DATE PATTERN (Check and add it) ")
                null
            }
        }
    } else null
}


// Check how to refactor it and let month have full name from date to string and string to date
fun dateToString_DayFullMonthNameYearFormat(dateToFormat: Date): String {
    return simpleDateFormat_DD_MMMMM_YYYY().format(dateToFormat)
}

fun dateFromStringNullCheck(value: String?): Date {
    return value?.stringAnyFormatToDefaultDateFormat() ?: Date()
}

fun dateToDayMonthYearFormatString(date: Date?): String? {
    date?.let { return simpleDateFormat_DD_MM_YYYY().format(date) } ?: return null
}

fun dateToYearMonthDayFormatString(date: Date?): String? {
    date?.let { return simpleDate_YYYY_MM_DD().format(date) } ?: return null
}

fun fixNumberOfMonth(monthNumber: Int): String {
    return when (monthNumber) {
        1, 2, 3, 4, 5, 6, 7, 8, 9 -> "0$monthNumber"
        else -> monthNumber.toString()
    }
}

fun getAnyDateIfStringContainsDate(stringToCheck: String): String? {
    val dateString_YYYY_MM_DD: String? = regexDatePattern_YYYY_MM_DD.find(stringToCheck)?.value
    val dateString_DD_MM_YYYY: String? = regexDatePattern_DD_MM_YYYY.find(stringToCheck)?.value
    return when {
        dateString_YYYY_MM_DD != null -> dateString_YYYY_MM_DD
        dateString_DD_MM_YYYY != null -> dateString_DD_MM_YYYY
        else -> null
    }
}


// DD is the Day of year
// dd is the Day of the month <- correct
// YYYY is week-based calendar year
// yyyy is ordinary calendar year <- correct
// MMMM is full month name <- correct
// MM is number of month <- correct
// MMM short name of month <- correct

fun simpleDateFormat_DD_MM_YYYY(): SimpleDateFormat {  // only number of month
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
}

fun simpleDate_YYYY_MM_DD(): SimpleDateFormat {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
}

fun simpleDateFormat_DD_MMMMM_YYYY(): SimpleDateFormat { // full month name
    return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
}

fun simpleDateMonthYearFormat(): SimpleDateFormat {
    return SimpleDateFormat("MMMM yyyy", Locale.getDefault())
}