package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.util.regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D
import piedel.piotr.thesis.util.regexOneToTenDigitsDotOrCommaThreeDigits
import piedel.piotr.thesis.util.regexOneToTenDigitsDotWhiteSpaceAndLetterA_D
import java.util.*


@Suppress("FunctionName")
class GoogleDriveResponseParser(googleDriveResponseHolder: GoogleDriveResponseHolder) {

    private var responseStringAfterOCRFromGDrive: String = googleDriveResponseHolder.plainTextFromOutputStream
    private var dateOnReceipt: Date? = null
    var dividedStringPublicForDebugging: List<String> = mutableListOf()
    private val googleDriveResponseParsedOperationsHolder = GoogleDriveResponseParsedOperationsHolder()


    fun parseStringFromOcrToListOfOperations(): List<Operation> {
        dateOnReceipt = getDateFromStringOrReturnTodayDate()
        parseStringFromOcrToListOfOperations(substringAfterWordsFiscalReceiptOrDefault())
        return googleDriveResponseParsedOperationsHolder.listOfParsedOperationsFromOCRString
    }

    private fun parseStringFromOcrToListOfOperations(responseString: String) {

        //TODO: Think how to redesign it?
        googleDriveResponseParsedOperationsHolder.addResultToOperationList(
                createOperationsFromListOfPairTitleValue(
                        createPairsTitleValueUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_D(responseString)))

        googleDriveResponseParsedOperationsHolder.addResultToOperationList(
                createOperationsFromListOfPairTitleValue(
                        createPairsTitleValueUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D(responseString)))

        googleDriveResponseParsedOperationsHolder.addResultToOperationList(
                createOperationsFromListOfPairTitleValue(
                        createPairsTitleValueUsingRegexOneToTenDigitsDotOrCommaThreeDigits(responseString)))
    }

    private fun createPairsTitleValueUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_D(responseString: String)
            : List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(
                    responseString, regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D))

    private fun createPairsTitleValueUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D(responseString: String)
            : List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(
                    responseString, regexOneToTenDigitsDotWhiteSpaceAndLetterA_D))

    private fun createPairsTitleValueUsingRegexOneToTenDigitsDotOrCommaThreeDigits(responseString: String)
            : List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(
                    responseString, regexOneToTenDigitsDotOrCommaThreeDigits))

}
