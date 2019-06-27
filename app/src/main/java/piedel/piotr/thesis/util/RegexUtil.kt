@file:Suppress("FunctionName")

package piedel.piotr.thesis.util

//README
// | -> or
// (?i) -> ignore case of words to detect


fun regexReceiptWord(): Regex = Regex("""\b(?i)(paragon fiskalny|fiskalny|paragon)\b""")

// (1-10 numbers)(,)(one or more spaces)(one letter A-D)
fun regexTwoNumberCommaWhiteSpaceAndLetterA_D(): Regex = Regex("""\b\d{1,10}[,]\d{2}\s*(?i)[A-D]{1}\b""")

// match (year{1 or 2}{and match 3 digits}) {- | / | \} (month{match 01-09 | 10-12}) {- | / | \} (day{01-09 | 1 or 2 and any digit(0-9)| 3 and (0 or 1)})
fun regexDatePattern_YYYY_MM_DD(): Regex = Regex("""([12]\d{3}([-\\\/])(0[1-9]|1[0-2])([-\\\/])(0[1-9]|[12]\d|3[01]))""")

// (day{01-09 | 1 or 2 and any digit(0-9)| 3 and (0 or 1)}) {- | / | \} (month{match 01-09 | 10-12}) {- | / | \} (year{1 or 2}{and match 3 digits})
fun regexDatePattern_DD_MM_YYYY(): Regex = Regex("""([0-2][0-9]|(3)[0-1])([-\\\/])(((0)[0-9])|((1)[0-2]))([-\\\/])\d{4}""")