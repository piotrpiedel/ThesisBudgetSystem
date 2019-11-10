package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.util.regexReceiptWord

class ResponseRegexSubstringer(private val stringToSplit: String) {

    fun substringAfterWordsFiscalReceiptOrReturnWholeString(): String {
        regexReceiptWord.find(stringToSplit).let { matchedResult ->
            return matchedResult?.let { notNullMatchResult ->
                // fun: return substring after occurrence of one of regex above to the end of string
                stringToSplit.substring(notNullMatchResult.range.last + 1, stringToSplit.lastIndex)
            } ?: stringToSplit // fun: if not found return an original string
        }
    }
}