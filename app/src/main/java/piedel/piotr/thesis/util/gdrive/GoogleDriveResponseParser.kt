package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.util.*
import java.util.*


@Suppress("FunctionName")
class GoogleDriveResponseParser(googleDriveResponseHolder: GoogleDriveResponseHolder) {

    private var responseStringAfterOCRFromGDrive: String = googleDriveResponseHolder.plainTextFromOutputStream
    private var dateOnReceipt: Date? = null
    var dividedStringPublicForDebugging: List<String> = mutableListOf()
    var listOfOperations: MutableList<Operation> = mutableListOf()
        private set

    init {
        dateOnReceipt = getDateFromStringOrReturnTodayDate()
        parseStringFromOcrToListOfOperations(substringAfterWordsFiscalReceiptOrDefault())
    }

    private fun parseStringFromOcrToListOfOperations(responseString: String) {
        addResultToOperationList(createOperationsFromListOfPairTitleValue(createPairsTitleValueUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_D(responseString)))
        addResultToOperationList(createOperationsFromListOfPairTitleValue(createPairsTitleValueUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D(responseString)))
        addResultToOperationList(createOperationsFromListOfPairTitleValue(createPairsTitleValueUsingRegexOneToTenDigitsDotOrCommaThreeDigits(responseString)))
    }

    private fun createPairsTitleValueUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_D(responseString: String): List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(responseString, regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D))

    private fun createPairsTitleValueUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D(responseString: String): List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(responseString, regexOneToTenDigitsDotWhiteSpaceAndLetterA_D))

    private fun createPairsTitleValueUsingRegexOneToTenDigitsDotOrCommaThreeDigits(responseString: String): List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(responseString, regexOneToTenDigitsDotOrCommaThreeDigits))

    private fun getDateFromStringOrReturnTodayDate() =
            getAnyDateIfStringContainsDate(responseStringAfterOCRFromGDrive)?.stringToDate()
                    ?: Date()

    private fun splitToStringListUsingRegexDelimiter(responseStringToSplit: String,
                                                     priceFormatRegex: Regex,
                                                     keepEmpty: Boolean = false,
                                                     isAddStringAfterLastMatch: Boolean = false): List<String> {
        val listOfStringDividedByDelimiter = mutableListOf<String>()
        var startPosition = 0   // Define var for substring start position
        priceFormatRegex.findAll(responseStringToSplit).forEach { foundMatchForPriceRegex ->
            // Looking for matches
            val stringBeforeCurrentMatch = findSubstringBeforeCurrentMatch(responseStringToSplit, startPosition, foundMatchForPriceRegex)
            if (stringBeforeCurrentMatch.isNotEmpty() || keepEmpty) {
                listOfStringDividedByDelimiter.add(stringBeforeCurrentMatch) // Adding substring before match start
            }
            deleteCharFromValueString(listOfStringDividedByDelimiter, foundMatchForPriceRegex)
            startPosition = getPositionOfNextSubstringAfterCurrentMatch(foundMatchForPriceRegex)
        }
        if (startPosition != responseStringToSplit.length && isAddStringAfterLastMatch)
            listOfStringDividedByDelimiter.add(getSubstringAfterLastMatchIfAny(responseStringToSplit, startPosition))
        return listOfStringDividedByDelimiter
    }

    private fun getSubstringAfterLastMatchIfAny(responseStringToSplit: String, startPosition: Int) =
            responseStringToSplit.substring(startPosition)

    private fun getPositionOfNextSubstringAfterCurrentMatch(foundMatchForPriceRegex: MatchResult) =
            foundMatchForPriceRegex.range.last() + 1  // Updating start pos of next substring before match

    private fun deleteCharFromValueString(listOfStringDividedByDelimiter: MutableList<String>, matchRegexResult: MatchResult) {
        listOfStringDividedByDelimiter.add(matchRegexResult.value.replace(Regex("(?i)[a-d]"), "")) // replacing value from #,## A -> #,## format
    }

    private fun findSubstringBeforeCurrentMatch(stringToSplit: String, startPosition: Int, matchRegexResult: MatchResult) =
            stringToSplit.substring(startPosition, matchRegexResult.range.first())

    private fun addResultToOperationList(operationList: List<Operation>) {
        if (!operationList.isNullOrEmpty()) {
            listOfOperations.addAll(operationList)
        } else return
    }

    private fun matchStringFromListToTitleValuePair(dividedString: List<String>): List<Pair<String, String>> {
        val pairTitleOperationValueOperation: List<Pair<String, String>>?
        if (!dividedString.isNullOrEmpty()) {
            pairTitleOperationValueOperation = dividedString.toPair()
        } else return emptyList()
        return pairTitleOperationValueOperation
    }

    private fun createOperationsFromListOfPairTitleValue(pairTitleOperationValueOperation: List<Pair<String, String>>): List<Operation> {
        val operationList: MutableList<Operation> = mutableListOf()
        for (pair in pairTitleOperationValueOperation) {
            operationList.add(createOperationFromPair(pair))
        }
        return operationList
    }

    private fun createOperationFromPair(pair: Pair<String, String>) =
            Operation(pair.second.parseStringWithCommaSeparatorToDouble(), pair.first, OperationType.OUTCOME, dateOnReceipt)

    private fun substringAfterWordsFiscalReceiptOrDefault(): String {
        val regex = regexReceiptWord
        regex.find(responseStringAfterOCRFromGDrive).let { matchedResult ->
            return matchedResult?.let { notNullMatchResult ->
                // fun: return substring after occurrence of one of regex above to the end of string
                responseStringAfterOCRFromGDrive.substring(notNullMatchResult.range.last + 1, responseStringAfterOCRFromGDrive.lastIndex)
            } ?: responseStringAfterOCRFromGDrive // fun: if not found return an original string
        }
    }
}
