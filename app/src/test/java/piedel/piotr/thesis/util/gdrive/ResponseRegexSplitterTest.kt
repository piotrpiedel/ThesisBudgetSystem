package piedel.piotr.thesis.util.gdrive

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import piedel.piotr.thesis.util.regexOneToTenDigitsCommaTwoDigits
import piedel.piotr.thesis.util.tokenize
import timber.log.Timber

internal class ResponseRegexSplitterTest {

    private lateinit var responseRegexSplitter: ResponseRegexSplitter
    val exampleResponseFirst = """A_WODA ZYWIEC ZD 6X 1*8, 9= 8,99 A C MLEKO SWIEZE 1L 3 1*3,05= 3,05 C C MLEKO SWIEZE 1L 3 C_SOK KUBUS 0,9L MA C SOK KUBUS 0,9L MA B CUKIER KROLEWSKI 1*2,49= 2:49 8
1*3,69= 3,69
"""

    val exampleResponseSecond = """A_WODA ZYWIEC ZD 6X 1*8, 9= 8,99 A C MLEKO SWIEZE 1L 3 1+3,05= 3,05 C C MLEKO SWIEZE 1L 3 1*3,05= 3,05 Č CSOK KUBUS 0,9L MA 1*3,69= C SOK KUBUS 0.9L MA
1*3,69= 3.69 B CUKIER KROLEWSKI
1*2,49= 2.49
оосооллоо
c00000
"""

    val exampleResponseThird = """"A_WODA ZYWIEC ZD 6X 1+8, 9= 8,99 A C MLEKO SWIEZE 1L 3 1*3,05= C MLEKO SWIEZE 1L 3 1*3,05= 3,05 C C SOK KUBUS 0.9L MA 1*3,69= 3, CSOK KUBUS 0,9L MA 1*3,69= 3. B_CUKIER KROLEWSKI 1+2,493 Ž. Sprzed. onod PIU A
DOVOLO
200000
"""

    @BeforeEach
    fun setUp() {
        responseRegexSplitter = ResponseRegexSplitter()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun splitStringToListUsingRegexOneToTenDigitsCommaWhiteSpaceAndLetterA_DFirst() {

    }

    @Test
    fun splitStringToListUsingRegexOneToTenDigitsDotWhiteSpaceAndLetterA_D() {
    }

    @Test
    fun splitStringToListUsingRegexOneToTenDigitsDotOrCommaThreeDigits() {
    }

    @Test
    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsComma() {

    }

    @Test
    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsDot() {
    }

    @Test
    fun tokenizeAndSplitStringToListUsingRegexOneToTenDigitsDotOrCommaThreeDigits() {
    }

    @Test
    fun splitToStringListUsingRegexDelimiter1() {
        val stringList = responseRegexSplitter.splitToStringListUsingRegexDelimiter(exampleResponseFirst.tokenize(), regexOneToTenDigitsCommaTwoDigits)
        println("tokenizeAndSplitStringToListUsingRegexOneToTenDigitsCommaTwoDigits $stringList")
    }

    @Test
    fun splitToStringListUsingRegexDelimiter2() {
        val stringList = responseRegexSplitter.splitToStringListUsingRegexDelimiter(exampleResponseSecond.tokenize(), regexOneToTenDigitsCommaTwoDigits)
        Timber.d("tokenizeAndSplitStringToListUsingRegexOneToTenDigitsCommaTwoDigits", stringList.toString())
    }

    @Test
    fun splitToStringListUsingRegexDelimiter3() {
        val stringList = responseRegexSplitter.splitToStringListUsingRegexDelimiter(exampleResponseThird.tokenize(), regexOneToTenDigitsCommaTwoDigits)
        Timber.d("tokenizeAndSplitStringToListUsingRegexOneToTenDigitsComma", stringList.toString())
    }

    @Test
    fun testFilterSingleCharTokensWhereLetterA_DFirst() {
        println(responseRegexSplitter.filterSingleCharTokensWhereLetterA_D(exampleResponseFirst.tokenize()))
    }

    @Test
    fun testFilterSingleCharTokensWhereLetterA_DSecond() {
        println(responseRegexSplitter.filterSingleCharTokensWhereLetterA_D(exampleResponseSecond.tokenize()))
    }

    @Test
    fun testFilterSingleCharTokensWhereLetterA_DThird() {
        println(responseRegexSplitter.filterSingleCharTokensWhereLetterA_D(exampleResponseThird.tokenize()))
    }
}