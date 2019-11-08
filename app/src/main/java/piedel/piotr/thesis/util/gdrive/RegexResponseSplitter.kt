package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.util.regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D
import piedel.piotr.thesis.util.regexOneToTenDigitsDotOrCommaThreeDigits
import piedel.piotr.thesis.util.regexOneToTenDigitsDotWhiteSpaceAndLetterA_D
import piedel.piotr.thesis.util.regexReceiptWord

class RegexResponseSplitter {

    private fun substringAfterWordsFiscalReceiptOrDefault(): String {
        regexReceiptWord.find(responseStringAfterOCRFromGDrive).let { matchedResult ->
            return matchedResult?.let { notNullMatchResult ->
                // fun: return substring after occurrence of one of regex above to the end of string
                responseStringAfterOCRFromGDrive.substring(notNullMatchResult.range.last + 1, responseStringAfterOCRFromGDrive.lastIndex)
            } ?: responseStringAfterOCRFromGDrive // fun: if not found return an original string
        }
    }

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

    private fun createPairsTitleValueUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_D(responseString: String): List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(responseString, regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D))

    private fun createPairsTitleValueUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D(responseString: String): List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(responseString, regexOneToTenDigitsDotWhiteSpaceAndLetterA_D))

    private fun createPairsTitleValueUsingRegexOneToTenDigitsDotOrCommaThreeDigits(responseString: String): List<Pair<String, String>> =
            matchStringFromListToTitleValuePair(splitToStringListUsingRegexDelimiter(responseString, regexOneToTenDigitsDotOrCommaThreeDigits))
}