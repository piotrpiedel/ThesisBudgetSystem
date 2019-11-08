@file:Suppress("FunctionName")

package piedel.piotr.thesis.util

//README
// | -> or
// (?i) -> ignore case of words to detect


val regexReceiptWord: Regex = Regex("""\b(?i)(paragon fiskalny|fiskalny|paragon)\b""")

// (1-10 digits)(,)(one or more spaces)(one letter A-D)
val regexOneToTenDigitsCommaWhiteSpaceAndLetterA_D: Regex = Regex("""\b\d{1,10}[,]\d{2}\s*(?i)[A-D]{1}\b""")

// (1-10 digits)(,)(one or more spaces)(one letter A-D)
val regexOneToTenDigitsDotWhiteSpaceAndLetterA_D: Regex = Regex("""\b\d{1,10}[.]\d{2}\s*(?i)[A-D]{1}\b""")

// (1-10 digits)(,|.)(3 digits)
val regexOneToTenDigitsDotOrCommaThreeDigits: Regex = Regex("""\b\d{1,10}[.,]\d{3}\b""")

// match (year{1 or 2}{and match 3 digits}) {- | / | \} (month{match 01-09 | 10-12}) {- | / | \} (day{01-09 | 1 or 2 and any digit(0-9)| 3 and (0 or 1)})
val regexDatePattern_YYYY_MM_DD: Regex = Regex("""([12]\d{3}([-\\\/])(0[1-9]|1[0-2])([-\\\/])(0[1-9]|[12]\d|3[01]))""")

// (day{01-09 | 1 or 2 and any digit(0-9)| 3 and (0 or 1)}) {- | / | \} (month{match 01-09 | 10-12}) {- | / | \} (year{1 or 2}{and match 3 digits})
val regexDatePattern_DD_MM_YYYY: Regex = Regex("""([0-2][0-9]|(3)[0-1])([-\\\/])(((0)[0-9])|((1)[0-2]))([-\\\/])\d{4}""")