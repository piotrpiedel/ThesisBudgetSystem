package piedel.piotr.thesis.util

import java.text.SimpleDateFormat
import java.util.*


fun simpleDateFormatDayMonthYear(): SimpleDateFormat {
    val myFormat = "dd MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.getDefault())
}

fun simpleDateYearMonthDay(): SimpleDateFormat {
    val myFormat = "yyyy-MM-dd"
    return SimpleDateFormat(myFormat, Locale.getDefault())
}

fun simpleDateMonthYearFormat(): SimpleDateFormat {
    val myFormat = "MMMM yyyy"
    return SimpleDateFormat(myFormat, Locale.getDefault())
}

fun dateFromYearMonthDayConverter(value: String?): Date? {
    return if (value?.isNotBlank() == true) {
        return simpleDateYearMonthDay().parse(value)
    } else {
        null
    }
}

fun dateFromDayMonthYearToYearMonthDay(value: String?): Date? {
    return if (value?.isNotBlank() == true) {
        val date = simpleDateFormatDayMonthYear().parse(value)
        val stringFromDate = simpleDateYearMonthDay().format(date)
        return simpleDateYearMonthDay().parse(stringFromDate)
    } else {
        null
    }
}

fun dateFromStringNullCheck(value: String): Date {
    return if (value.isNotBlank()) {
        simpleDateFormatDayMonthYear().parse(value)
    } else return Date()
}

fun dateToDayMonthYearFormatString(date: Date?): String? {
    date?.let { return simpleDateFormatDayMonthYear().format(date) } ?: return null
}

fun dateToYearMonthDayFormatString(date: Date?): String? {
    date?.let { return simpleDateYearMonthDay().format(date) } ?: return null
}


fun fixNumberOfMonth(monthNumber: Int): String {
    return when (monthNumber) {
        1, 2, 3, 4, 5, 6, 7, 8, 9 -> "0" + monthNumber.toString()
        else -> monthNumber.toString()
    }
}