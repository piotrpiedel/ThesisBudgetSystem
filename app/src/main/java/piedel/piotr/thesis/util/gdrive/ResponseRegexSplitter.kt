@file:Suppress("FunctionName")

package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.util.*

class ResponseRegexSplitter {

    fun splitStringToListUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_D(stringToSplit: String): List<String> =
            (splitToStringListUsingRegexDelimiter(stringToSplit, regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D))

    fun splitStringToListUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit, regexOneToTenDigitsDotWhiteSpaceAndLetterA_D)

    fun splitStringToListUsingRegexOneToTenDigitsDotOrCommaThreeDigits(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit, regexOneToTenDigitsDotOrCommaThreeDigits)

    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsComma(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit.tokenize(), regexOneToTenDigitsComma)

    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsDot(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit.tokenize(), regexOneToTenDigitsDot)

    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsDotOrCommaThreeDigits(stringToSplit: String): List<String> =
            splitToStringListUsingRegexDelimiter(stringToSplit.tokenize(), regexOneToTenDigitsDotOrCommaThreeDigits)

    private fun splitToStringListUsingRegexDelimiter(listOfStringTokensFromResponse: List<String>, priceFormatRegex: Regex): List<String> {
        val listOfFinallySplitStrings: MutableList<String> = mutableListOf()
        val temporaryList: MutableList<String> = mutableListOf()
        var priceValueMatches: Int = computeHowManyPricesMatchesInList(listOfStringTokensFromResponse)
        return listOfFinallySplitStrings
    }

    private fun computeHowManyPricesMatchesInList(listOfStringTokensFromResponse: List<String>): Int {
        var priceValueMatches: Int = 0
        for (tokenFromList in listOfStringTokensFromResponse) {
            if (isTokenMatchingPriceValueFormat(tokenFromList, regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D)) {
                priceValueMatches++
            } else if (isTokenMatchingPriceValueFormat(tokenFromList, regexOneToTenDigitsDotWhiteSpaceAndLetterA_D)) {
                priceValueMatches++
            } else if (isTokenMatchingPriceValueFormat(tokenFromList, regexOneToTenDigitsDotOrCommaThreeDigits)) {
                priceValueMatches++
            }
        }
        return priceValueMatches
    }

    private fun isTokenMatchingPriceValueFormat(tokenFromList: String, priceFormatRegex: Regex) =
            tokenFromList.contains(priceFormatRegex)


//        for (tokenFromList in listOfStringTokensFromResponse) {
//            if (tokenFromList.contains(priceFormatRegex)) {
//                if (temporaryList.isNotEmpty()) {
//                    listOfFinallySplitStrings.add(temporaryList.joinToString())
//                    listOfFinallySplitStrings.add(priceFormatRegex.find(tokenFromList)?.value.toString())
//                }
//                temporaryList.clear()
//            } else {
//                temporaryList.add(tokenFromList)
//            }
//        }
//        return listOfFinallySplitStrings

    private fun splitToStringListUsingRegexDelimiter(responseStringToSplit: String, priceFormatRegex: Regex,
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