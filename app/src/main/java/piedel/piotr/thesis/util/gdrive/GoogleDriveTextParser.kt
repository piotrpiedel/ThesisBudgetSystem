package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.util.getAnyDateIfStringContainsDate
import piedel.piotr.thesis.util.stringToDate
import java.util.*

class GoogleDriveTextParser(private val googleDriveResponseHolder: GoogleDriveResponseHolder) {
    private var responseString: String = googleDriveResponseHolder.plainTextFromOutputStream
    private var date: Date? = null

    init {
        date = Date().stringToDate(getAnyDateIfStringContainsDate(responseString).orEmpty())
        responseString = substringAfterWordsFiscalReceipt()
        val dividedString = splitStringToListWithRegexPattern(responseString)
    }

    private fun substringAfterWordsFiscalReceipt(): String {
        val regex = Regex("""\b(?i)(paragon fiskalny|fiskalny|paragon)\b""") // (?i) -> ignore case of words to detect
        regex.find(responseString).let { matchedResult ->
            return matchedResult?.let { notNullMatchResult ->
                // fun: return substring after occurrence of one of regex above to the end of string
                responseString.substring(notNullMatchResult.range.last + 1, responseString.lastIndex)
            } ?: responseString // fun: if not found return an original string
        }
    }

    private fun splitStringToListWithRegexPattern(substring: String): List<String> {
        val regexString = """\b\d{1,10}[,]\d{2}\s*(?i)[A-D]{1}\b"""  // (1-10 numbers)(,)(one or more spaces)(one letter A-D)
        return splitStringKeepingDelimiters(substring, (Regex(regexString)))
    }

    private fun splitStringKeepingDelimiters(stringToSplit: String, regex: Regex, keep_empty: Boolean = false, addStringAfterLastMatch: Boolean = false): List<String> {
        val listOfStringDividedByDelimiter = mutableListOf<String>() // Declare the mutable list var
        var start = 0                     // Define var for substring start pos
        regex.findAll(stringToSplit).forEach { matchRegexResult ->
            // Looking for matches
            val substringBefore = stringToSplit.substring(start, matchRegexResult.range.first()) // // Substring before match start
            if (substringBefore.isNotEmpty() || keep_empty) {
                listOfStringDividedByDelimiter.add(substringBefore)      // Adding substring before match start
            }
            listOfStringDividedByDelimiter.add(matchRegexResult.value)
            start = matchRegexResult.range.last() + 1       // Updating start pos of next substring before match
        }
        if (start != stringToSplit.length && addStringAfterLastMatch)
            listOfStringDividedByDelimiter.add(stringToSplit.substring(start))  // Adding text after last match if any

        return listOfStringDividedByDelimiter
    }
}
