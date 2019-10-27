package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.util.*
import java.util.*


@Suppress("FunctionName")
class GoogleDriveResponseParser(googleDriveResponseHolder: GoogleDriveResponseHolder) {

    private var responseString: String = googleDriveResponseHolder.plainTextFromOutputStream
    private var date: Date? = null
    var dividedStringPublicForDebugging: List<String> = mutableListOf()
    var listOfOperations: MutableList<Operation> = mutableListOf()
        private set


    //TODO: work in here is to add new regex, refactor this, code to better understandability
    // TODO: replace all those functions with functions passing as paramter

    init {
        date = getAnyDateIfStringContainsDate(responseString)?.stringToDate() ?: Date()
        val responseStringSubstring = substringAfterWordsFiscalReceiptOrDefault()
        addResultToOperationList(parseOCRDriveOutputToOperations(firstRegex(responseStringSubstring)))
        addResultToOperationList(parseOCRDriveOutputToOperations(secondRegexIfFirstFails(responseStringSubstring)))
        addResultToOperationList(parseOCRDriveOutputToOperations(thirdRegexOnlyIfSecondAndFirstFails(responseStringSubstring)))
    }

    private fun firstRegex(responseStringSubstring: String): List<String> {
        return splitToListWithRegexPattern(responseStringSubstring, regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D())
    }

    private fun secondRegexIfFirstFails(responseStringSubstring: String): List<String> {
        return splitToListWithRegexPattern(responseStringSubstring, regexOneToTenDigitsDotWhiteSpaceAndLetterA_D())
    }

    private fun thirdRegexOnlyIfSecondAndFirstFails(responseStringSubstring: String): List<String> {
        return splitToListWithRegexPattern(responseStringSubstring, regexOneToTenDigitsDotOrCommaWThreeDigits())
    }

    private fun splitToListWithRegexPattern(responseString: String, regex: Regex): List<String> {
        return splitStringKeepingDelimiters(responseString, regex)
    }

    private fun addResultToOperationList(operationList: List<Operation>) {
        if (!operationList.isNullOrEmpty()) {
            listOfOperations.addAll(operationList)
        } else return
    }

    private fun parseOCRDriveOutputToOperations(dividedString: List<String>): List<Operation> {
        val pairTitleOperationValueOperation: List<Pair<String, String>>
        val operationList: MutableList<Operation> = mutableListOf()
        if (!dividedString.isNullOrEmpty()) {
            pairTitleOperationValueOperation = dividedString.toPair()
            for (pair in pairTitleOperationValueOperation) {
                operationList.add(createOperationFromPair(pair))
            }
        }
        return operationList
    }

    private fun createOperationFromPair(pair: Pair<String, String>) =
            Operation(pair.second.parseStringWithCommaSeparatorToDouble(), pair.first, OperationType.OUTCOME, date)

    private fun substringAfterWordsFiscalReceiptOrDefault(): String {
        val regex = regexReceiptWord()
        regex.find(responseString).let { matchedResult ->
            return matchedResult?.let { notNullMatchResult ->
                // fun: return substring after occurrence of one of regex above to the end of string
                responseString.substring(notNullMatchResult.range.last + 1, responseString.lastIndex)
            } ?: responseString // fun: if not found return an original string
        }
    }


    private fun splitStringKeepingDelimiters(stringToSplit: String, regex: Regex, keep_empty: Boolean = false, addStringAfterLastMatch: Boolean = false): List<String> {
        val listOfStringDividedByDelimiter = mutableListOf<String>() // Declare the mutable list var
        var start = 0                     // Define var for substring start position
        regex.findAll(stringToSplit).forEach { matchRegexResult ->
            // Looking for matches
            val substringBefore = stringToSplit.substring(start, matchRegexResult.range.first()) // // Substring before match start
            if (substringBefore.isNotEmpty() || keep_empty) {
                listOfStringDividedByDelimiter.add(substringBefore)      // Adding substring before match start
            }
            listOfStringDividedByDelimiter.add(matchRegexResult.value.replace(Regex("(?i)[a-d]"), "")) // replacing value from #,## A -> #,## format
            start = matchRegexResult.range.last() + 1       // Updating start pos of next substring before match
        }
        if (start != stringToSplit.length && addStringAfterLastMatch)
            listOfStringDividedByDelimiter.add(stringToSplit.substring(start))  // Adding text after last match if any

        return listOfStringDividedByDelimiter
    }
}
