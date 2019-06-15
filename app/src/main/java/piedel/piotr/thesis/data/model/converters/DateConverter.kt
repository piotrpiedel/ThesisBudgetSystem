package piedel.piotr.thesis.data.model.converters

import androidx.room.TypeConverter
import piedel.piotr.thesis.util.dateToYearMonthDayFormatString
import piedel.piotr.thesis.util.stringToDate
import java.util.*

// desired format in app and database is yyyy-MM-DD
class DateConverter {

    @TypeConverter
    fun fromString(value: String?): Date? {
        return Date().stringToDate(value)
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return dateToYearMonthDayFormatString(date)
    }
}