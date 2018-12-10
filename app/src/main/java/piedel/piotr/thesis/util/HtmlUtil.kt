package piedel.piotr.thesis.util

import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import timber.log.Timber
import java.io.File

fun parseHTMLFileToJsonArray(path: String): JSONArray {
    val fileToParse = File(path)

    val jsonArray = JSONArray()
    if (fileToParse.exists()) {

        val htmlFile: Document = Jsoup.parse(fileToParse, "UTF-8") // get HTML file to String

        val innerHTMLSignsString = htmlFile.getElementsByClass("data").html() //@return string of all element's inner HTML

        val listOfStringsToParse = parseStringToStringArray(innerHTMLSignsString.substring(innerHTMLSignsString.lastIndexOf("</table>") + 9))

        parseToJson(listOfStringsToParse, jsonArray)

    } else {
        Timber.d("The file does not exist")
    }
    return jsonArray
}