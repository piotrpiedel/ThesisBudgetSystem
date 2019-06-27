package piedel.piotr.thesis.util

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

fun String.suffixAppendToFileNameBeforeExtension(suffixToFile: String): String {
    val dotIndex = this.lastIndexOf(".");
    return if (dotIndex == -1) this + suffixToFile;
    else this.substring(0, dotIndex) + suffixToFile + this.substring(dotIndex);
}

fun doubleToStringInTwoPlacesAfterComma(doubleToFormat: Double?): String {
    val decimalFormat = DecimalFormat("##.##")
    decimalFormat.roundingMode = RoundingMode.FLOOR
    if (doubleToFormat != null) {
        return decimalFormat.format(doubleToFormat)
    } else return ""

}

fun parseStringToStringArray(data: String): List<List<String>> {

    val listOfListOperationsInStringArray: List<List<String>> = (data.lines() as ArrayList<String>).chunked(9)

    return replaceHTMLSignsFromArrayListOfStrings(listOfListOperationsInStringArray)
}


fun replaceHTMLSignsFromArrayListOfStrings(passedListOfList: List<List<String>>): List<List<String>> {

    val listOfArrayList: ArrayList<ArrayList<String>> = arrayListOf()

    for (innerList in passedListOfList) {

        val innerListAfterReplacing: ArrayList<String> = arrayListOf()

        for (itemInInnerList in innerList) {
            if (!itemInInnerList.contains("nobr")) {
                var stringWithReplacedHTMLSigns = itemInInnerList.replace("<br>", " ")
                stringWithReplacedHTMLSigns = takeFirstEightWordsFromString(stringWithReplacedHTMLSigns)
                innerListAfterReplacing.add(stringWithReplacedHTMLSigns)
            }

        }
        listOfArrayList.add(passedListOfList.indexOf(innerList), innerListAfterReplacing)
    }
    return listOfArrayList
}

fun takeFirstEightWordsFromString(phraseToShorten: String): String {
    val stringTokenizerOfPassedPhrase = StringTokenizer(phraseToShorten)
    val shortenedStringBuilder = StringBuilder()
    var counter = 0
    do {
        if (stringTokenizerOfPassedPhrase.hasMoreTokens()) {
            shortenedStringBuilder.append(stringTokenizerOfPassedPhrase.nextToken() + " ")
            counter++
        } else counter = 8
    } while (counter < 8)
    return shortenedStringBuilder.toString()
}