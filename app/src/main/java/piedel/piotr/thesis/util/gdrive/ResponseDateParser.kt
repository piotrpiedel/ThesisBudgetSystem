package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.util.getAnyDateIfStringContainsDate
import piedel.piotr.thesis.util.stringAnyFormatToDefaultDateFormat
import java.util.*

class ResponseDateParser(private val stringToGetDateFrom: String) {

    fun getDateFromStringOrReturnTodayDate(): Date =
            getAnyDateIfStringContainsDate(stringToGetDateFrom)?.stringAnyFormatToDefaultDateFormat()
                    ?: Date()
}