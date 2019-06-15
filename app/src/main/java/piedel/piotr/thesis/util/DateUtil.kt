@file:Suppress("FunctionName", "LocalVariableName")

package piedel.piotr.thesis.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun Date.stringToDate(stringDateToParse: String?): Date? {
    val datePattern_YYYY_MM_DD = """([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))"""
    val datePattern_DD_MM_YYYY = """([0-2][0-9]|(3)[0-1])(-)(((0)[0-9])|((1)[0-2]))(-)\d{4}"""
    return if (!stringDateToParse.isNullOrBlank()) {
        when {
            stringDateToParse.contains(Regex(datePattern_YYYY_MM_DD)) -> simpleDate_YYYY_MM_DD().parse(stringDateToParse)
            stringDateToParse.contains(Regex(datePattern_DD_MM_YYYY)) ->
                simpleDate_YYYY_MM_DD().parse( // parse return Date in desired pattern  yyyy-MM-DD
                        simpleDate_YYYY_MM_DD().format( // format return String in desired  pattern yyyy-MM-DD
                                simpleDateFormat_DD_MM_YYYY().parse(stringDateToParse))) // parse return Date in current
            else -> {
                Timber.e("DateUtil fun stringToDate() NOT SUPPORTED DATE PATTERN (Check and add it) ")
                null
            }
        }
    } else null
}

fun dateToString_DayFullMonthNameYearFormat(dateToFormat: Date): String {
    return simpleDateFormat_DD_MMMMM_YYYY().format(dateToFormat)
}

fun dateFromStringNullCheck(value: String): Date {
    return Date().stringToDate(value) ?: Date()
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
    // | means or
    // match (year{1 or 2}{and match 3 digits}) {-} (month{match 01-09 | 10-12}) {-} (day{01-09 | 1 or 2 and any digit(0-9)| 3 and (0 or 1)})
    val datePattern_YYYY_MM_DD = """([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))"""

    // (day{01-09 | 1 or 2 and any digit(0-9)| 3 and (0 or 1)}) {-} (month{match 01-09 | 10-12}) {-} (year{1 or 2}{and match 3 digits})
    val datePattern_DD_MM_YYYY = """([0-2][0-9]|(3)[0-1])(-)(((0)[0-9])|((1)[0-2]))(-)\d{4}"""
    val regexDate_YYYY_MM_DD: String? = Regex(datePattern_YYYY_MM_DD).find(stringToCheck)?.value
    val regexDate_DD_MM_YYYY: String? = Regex(datePattern_DD_MM_YYYY).find(stringToCheck)?.value
    return when {
        regexDate_YYYY_MM_DD != null -> regexDate_YYYY_MM_DD
        regexDate_DD_MM_YYYY != null -> regexDate_DD_MM_YYYY
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
    val myFormat = "dd-MM-yyyy"
    return SimpleDateFormat(myFormat, Locale.getDefault())
}

fun simpleDate_YYYY_MM_DD(): SimpleDateFormat {
    val myFormat = "yyyy-MM-dd"
    return SimpleDateFormat(myFormat, Locale.getDefault())
}

fun simpleDateFormat_DD_MMMMM_YYYY(): SimpleDateFormat { // full month name
    val myFormat = "dd MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.getDefault())
}

fun simpleDateMonthYearFormat(): SimpleDateFormat {
    val myFormat = "MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.getDefault())
}