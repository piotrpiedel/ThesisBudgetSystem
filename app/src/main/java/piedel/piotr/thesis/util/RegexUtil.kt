@file:Suppress("FunctionName")

package piedel.piotr.thesis.util

//README
// | -> or
// (?i) -> ignore case of words to detect

// (one letter [a-d][A-D])
val regexLettersFromAtoDIgnoreCase = Regex("(?i)[a-d]")

// (one letter [a-d][A-D])
val regexSingleDigitZeroToNine = Regex("[0-9]")

// (one letter [a-d][A-D])
val regexLetterOrDigit = Regex("[a-zA-Z0-9]")

val regexReceiptWord: Regex = Regex("""\b(?i)(paragon fiskalny|fiskalny|paragon)\b""")

// (1-10 digits)(,)(one or more spaces)(one letter A-D)
val regexOneToTenDigitsCommaTwoDigitsWhiteSpaceAndLetterA_D: Regex = Regex("""\b\d{1,10}[,]\d{2}\s*(?i)[A-D]{1}\b""")

// (1-10 digits)(,)(one or more spaces)(one letter A-D)
val regexOneToTenDigitsDotTwoDigitsWhiteSpaceAndLetterA_D: Regex = Regex("""\b\d{1,10}[.]\d{2}\s*(?i)[A-D]{1}\b""")

// (1-10 digits)(,)(one or more spaces)(one letter A-D)
val regexOneToTenDigitsCommaTwoDigits: Regex = Regex("""\b\d{1,10}[,]\d{2}""")

// (1-10 digits)(,)(one or more spaces)(one letter A-D)
val regexOneToTenDigitsDotTwoDigits: Regex = Regex("""\b\d{1,10}[.]\d{2}""")

// (1-10 digits)(,|.)(3 digits)
val regexOneToTenDigitsDotOrCommaThreeDigits: Regex = Regex("""\b\d{1,10}[.,]\d{3}\b""")

// match (year{1 or 2}{and match 3 digits}) {- | / | \} (month{match 01-09 | 10-12}) {- | / | \} (day{01-09 | 1 or 2 and any digit(0-9)| 3 and (0 or 1)})
val regexDatePattern_YYYY_MM_DD: Regex = Regex("""([12]\d{3}([-\\\/])(0[1-9]|1[0-2])([-\\\/])(0[1-9]|[12]\d|3[01]))""")

// (day{01-09 | 1 or 2 and any digit(0-9)| 3 and (0 or 1)}) {- | / | \} (month{match 01-09 | 10-12}) {- | / | \} (year{1 or 2}{and match 3 digits})
val regexDatePattern_DD_MM_YYYY: Regex = Regex("""([0-2][0-9]|(3)[0-1])([-\\\/])(((0)[0-9])|((1)[0-2]))([-\\\/])\d{4}""")