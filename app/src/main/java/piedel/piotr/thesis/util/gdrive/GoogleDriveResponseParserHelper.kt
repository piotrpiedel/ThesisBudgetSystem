package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.data.model.operation.Operation
import java.util.*


@Suppress("FunctionName")
class GoogleDriveResponseParserHelper(googleDriveResponseHolder: GoogleDriveResponseHolder) {

    private var responseOCRedStringFromGDrive: String = googleDriveResponseHolder.plainTextFromOutputStream
    private var dateOnReceipt: Date? = null
    var dividedStringPublicForDebugging: List<String> = mutableListOf()
    private val googleDriveResponseParsedOperationsHolder = ParsedOperationsHolder()


    fun parseStringFromOcrToListOfOperations(): List<Operation> {
        dateOnReceipt = ResponseDateParser(responseOCRedStringFromGDrive)
                .getDateFromStringOrReturnTodayDate()

        parseStringFromOcrToListOfOperations(ResponseRegexSubstringer(responseOCRedStringFromGDrive)
                .substringAfterWordsFiscalReceiptOrReturnWholeString())

        return googleDriveResponseParsedOperationsHolder.listOfParsedOperationsFromOCRString
    }

    private fun parseStringFromOcrToListOfOperations(responseString: String) {
        val responseRegexSplitter = ResponseRegexSplitter()
        addOperationsToResult(responseString, responseRegexSplitter::splitStringToListUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_D)
        addOperationsToResult(responseString, responseRegexSplitter::splitStringToListUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D)
        addOperationsToResult(responseString, responseRegexSplitter::splitStringToListUsingRegexOneToTenDigitsDotOrCommaThreeDigits)
    }

    private fun addOperationsToResult(responseString: String, stringSplitterWithRegexFunction: (responseString: String) -> List<String>) {
        googleDriveResponseParsedOperationsHolder
                .addResultToOperationList(
                        PairOfStringToOperationConverter()
                                .matchPairTitleValueToListOfOperation(
                                        ResponsePairsMatcher()
                                                .matchStringFromListToTitleValuePair(stringSplitterWithRegexFunction(responseString))
                                        , dateOnReceipt))
    }

}
