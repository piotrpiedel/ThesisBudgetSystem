package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.util.deleteCharFromPriceValueString
import piedel.piotr.thesis.util.regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D
import piedel.piotr.thesis.util.regexOneToTenDigitsDotOrCommaThreeDigits
import piedel.piotr.thesis.util.regexOneToTenDigitsDotWhiteSpaceAndLetterA_D

class ResponseRegexSplitter {

    fun splitStringToListUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_D(stringToSplit: String): List<String> =
            (splitToStringListUsingRegexDelimiter(stringToSplit, regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D))

    fun splitStringToListUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit, regexOneToTenDigitsDotWhiteSpaceAndLetterA_D)

    fun splitStringToListUsingRegexOneToTenDigitsDotOrCommaThreeDigits(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit, regexOneToTenDigitsDotOrCommaThreeDigits)

    private fun splitToStringListUsingRegexDelimiter(responseStringToSplit: String,
                                                     priceFormatRegex: Regex,
                                                     keepEmpty: Boolean = false,
                                                     isAddStringAfterLastMatch: Boolean = false): List<String> {
        val listOfStringDividedByDelimiter = mutableListOf<String>()
        var startPosition = 0   // Define var for substring start position
        priceFormatRegex.findAll(responseStringToSplit).forEach { foundMatchForPriceRegex ->
            // Looking for matches
            val stringBeforeCurrentMatch = findSubstringBeforeCurrentMatch(responseStringToSplit, startPosition,
                    foundMatchForPriceRegex)

            if (stringBeforeCurrentMatch.isNotEmpty() || keepEmpty) {
                listOfStringDividedByDelimiter.add(stringBeforeCurrentMatch) // Adding substring before match start
            }

            addPriceValueToList(listOfStringDividedByDelimiter, foundMatchForPriceRegex)

            startPosition = getPositionOfNextSubstringAfterCurrentMatch(foundMatchForPriceRegex)
        }
        if (startPosition != responseStringToSplit.length && isAddStringAfterLastMatch)
            listOfStringDividedByDelimiter.add(getSubstringAfterLastMatchIfAny(responseStringToSplit, startPosition))
        return listOfStringDividedByDelimiter
    }

    private fun addPriceValueToList(listOfStringDividedByDelimiter: MutableList<String>,
                                    foundMatchForPriceRegex: MatchResult) {
        listOfStringDividedByDelimiter.add(deleteCharFromPriceValueString(foundMatchForPriceRegex))
    }

    private fun getSubstringAfterLastMatchIfAny(responseStringToSplit: String, startPosition: Int) =
            responseStringToSplit.substring(startPosition)

    private fun getPositionOfNextSubstringAfterCurrentMatch(foundMatchForPriceRegex: MatchResult) =
            foundMatchForPriceRegex.range.last() + 1  // Updating start pos of next substring before match


    private fun findSubstringBeforeCurrentMatch(stringToSplit: String, startPosition: Int, matchRegexResult: MatchResult) =
            stringToSplit.substring(startPosition, matchRegexResult.range.first())

}