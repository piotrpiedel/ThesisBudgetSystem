package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.util.getAnyDateIfStringContainsDate
import piedel.piotr.thesis.util.parseStringWithCommaSeparatorToDouble
import piedel.piotr.thesis.util.regexReceiptWord
import piedel.piotr.thesis.util.regexTwoNumberCommaWhiteSpaceAndLetterA_D
import piedel.piotr.thesis.util.stringToDate
import piedel.piotr.thesis.util.toPair
import java.util.*


class GoogleDriveResponseParser(googleDriveResponseHolder: GoogleDriveResponseHolder) {

    private var responseString: String = googleDriveResponseHolder.plainTextFromOutputStream
    private var date: Date? = null
    var listOfOperations: MutableList<Operation> = mutableListOf()
        private set

    init {
        date = getAnyDateIfStringContainsDate(responseString)?.stringToDate() ?: Date()
        responseString = substringAfterWordsFiscalReceipt()
        val dividedString: List<String> = splitStringToListWithRegexPattern(responseString)
        val operationList = parseOCRDriveOutputToOperations(dividedString)
        addResultToOperationList(operationList)
    }

    private fun addResultToOperationList(operationList: List<Operation>) {
        if (!operationList.isNullOrEmpty()) {
            listOfOperations.addAll(operationList)
        } else return
    }

    private fun parseOCRDriveOutputToOperations(dividedString: List<String>): List<Operation> {
        var pairTitleOperationValueOperation: List<Pair<String, String>>
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

    private fun substringAfterWordsFiscalReceipt(): String {
        val regex = regexReceiptWord()
        regex.find(responseString).let { matchedResult ->
            return matchedResult?.let { notNullMatchResult ->
                // fun: return substring after occurrence of one of regex above to the end of string
                responseString.substring(notNullMatchResult.range.last + 1, responseString.lastIndex)
            } ?: responseString // fun: if not found return an original string
        }
    }

    private fun splitStringToListWithRegexPattern(substring: String): List<String> {
        val regex = regexTwoNumberCommaWhiteSpaceAndLetterA_D()
        return splitStringKeepingDelimiters(substring, regex)
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
