package piedel.piotr.thesis.util

import java.text.NumberFormat
import java.util.*

fun getRandomCategory(): Int {
    return (7..37).random()
}

fun String.parseStringWithCommaSeparatorToDouble(): Double {
    return NumberFormat.getInstance(Locale.FRANCE).parse(this).toDouble()
}