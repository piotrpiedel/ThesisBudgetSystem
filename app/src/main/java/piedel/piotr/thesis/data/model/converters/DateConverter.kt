package piedel.piotr.thesis.data.model.converters

import androidx.room.TypeConverter
import piedel.piotr.thesis.util.dateFromYearMonthDayConverter
import piedel.piotr.thesis.util.dateToYearMonthDayFormatString
import java.util.Date

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return if (value == null) null else {
            return dateFromYearMonthDayConverter(value)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return dateToYearMonthDayFormatString(date)
    }
}