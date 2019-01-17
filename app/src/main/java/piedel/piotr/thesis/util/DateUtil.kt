package piedel.piotr.thesis.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun simpleDateFormat(): SimpleDateFormat {
    val myFormat = "dd MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.ENGLISH)
}

fun simpleDateFormatDayMonthYear(): SimpleDateFormat {
    val myFormat = "dd MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.ENGLISH)
}

fun simpleDateFormatFromJson(): SimpleDateFormat {
    val myFormat = "yyyy-MM-dd"
    return SimpleDateFormat(myFormat, Locale.ENGLISH)
}

fun simpleDateMonthYearFormat(): SimpleDateFormat {
    val myFormat = "MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.ENGLISH)
}


fun simpleDate_YEAR_MONTH_DAY(): SimpleDateFormat {
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

fun dateFrom_YEAR_MONTH_DAY_Converter(value: String?): Date? {
    return if (value?.isNotBlank() == true) {
        return simpleDate_YEAR_MONTH_DAY().parse(value)
    } else {
        null
    }
}


fun dateFrom_DAY_MONTH_YEAR_TO_YEAR_MONT_DAY(value: String?): Date? {
    return if (value?.isNotBlank() == true) {
        val date = simpleDateFormatDayMonthYear().parse(value)
        val stringFromDate = simpleDate_YEAR_MONTH_DAY().format(date)
        return simpleDate_YEAR_MONTH_DAY().parse(stringFromDate)
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

fun dateToTextString_YEAR_MONTH_DAY(date: Date?): String? {
    date?.let { return simpleDate_YEAR_MONTH_DAY().format(date) } ?: return null
}


fun fixNumberOfMonth(monthNumber: Int): String {
    return when (monthNumber) {
        1, 2, 3, 4, 5, 6, 7, 8, 9 -> "0" + monthNumber.toString()
        else -> monthNumber.toString()
    }
}