@file:Suppress("FunctionName")

package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.util.*

class ResponseRegexSplitter {

    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsComma(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit.tokenize(), regexOneToTenDigitsCommaTwoDigits)

    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsDot(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit.tokenize(), regexOneToTenDigitsDotTwoDigits)

    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsDotOrCommaThreeDigits(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit.tokenize(), regexOneToTenDigitsDotOrCommaThreeDigits)

    fun splitToStringListUsingRegexDelimiter(listOfStringTokensFromResponse: List<String>, priceFormatRegex: Regex): List<String> {
        val filteredListRemovedLettersAD: List<String> = filterSingleCharTokensWhereLetterA_D(listOfStringTokensFromResponse)
        val listOfFinallySplitStrings: MutableList<String> = mutableListOf()
        val temporaryList: MutableList<String> = mutableListOf()
        for (tokenFromList in listOfStringTokensFromResponse) {
            if (tokenFromList.contains(priceFormatRegex)) {
                if (temporaryList.isNotEmpty()) {
                    listOfFinallySplitStrings.add(temporaryList.joinToString())
                    listOfFinallySplitStrings.add(priceFormatRegex.find(tokenFromList)?.value.toString())
                }
                temporaryList.clear()
            } else {
                temporaryList.add(tokenFromList)
            }
        }
        return listOfFinallySplitStrings
    }

    fun filterSingleCharTokensWhereLetterA_D(listOfStringTokensFromResponse: List<String>): List<String> {
        return listOfStringTokensFromResponse
                .filter { token -> !token.matches(regexLettersFromAtoDIgnoreCase) }
                .filter { token -> !token.matches(regexSingleDigitZeroToNine) }
                .filter { token -> token.isNotBlank() }
//                .filter { token -> token.length > 1 }
    }


    private fun isTokenMatchingPriceValueFormat(tokenFromList: String, priceFormatRegex: Regex) = priceFormatRegex.findAll(tokenFromList, 0).count()
//    private fun isTokenMatchingPriceValueFormat(tokenFromList: String, priceFormatRegex: Regex) = tokenFromList.contains(priceFormatRegex)


    private fun splitToStringListUsingRegexDelimiter2(responseStringToSplit: String, priceFormatRegex: Regex,
                                                      keepEmpty: Boolean = false, isAddStringAfterLastMatch: Boolean = false): List<String> {
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