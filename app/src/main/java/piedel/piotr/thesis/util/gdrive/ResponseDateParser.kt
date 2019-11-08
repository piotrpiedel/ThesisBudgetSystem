package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.util.getAnyDateIfStringContainsDate
import piedel.piotr.thesis.util.stringToDate
import java.util.*

class ResponseDateParser {

    private fun getDateFromStringOrReturnTodayDate() =
            getAnyDateIfStringContainsDate(responseStringAfterOCRFromGDrive)?.stringToDate()
                    ?: Date()

}