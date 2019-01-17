package piedel.piotr.thesis.data.model.converters

import androidx.room.TypeConverter
import piedel.piotr.thesis.util.dateFrom_YEAR_MONTH_DAY_Converter
import piedel.piotr.thesis.util.dateToTextString_YEAR_MONTH_DAY
import java.util.Date

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return if (value == null) null else {
            return dateFrom_YEAR_MONTH_DAY_Converter(value)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return dateToTextString_YEAR_MONTH_DAY(date)
    }
}