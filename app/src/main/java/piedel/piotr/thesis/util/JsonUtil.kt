package piedel.piotr.thesis.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


fun parseToJson(readyStringToParseToJson: List<List<String>>, jsonArray: JSONArray) {

    for (innerList in readyStringToParseToJson) {
        val jsonObject = JSONObject()
        for (innerIterator in innerList.indices) {
            when (innerIterator) {//TODO: change removing prefix
                1 -> jsonObject.put("date", innerList[0])
                2 -> jsonObject.put("title", innerList[2])
                3 -> jsonObject.put("value", innerList[3])
            }
        }
        if (innerList[3].contains("-")) {
            jsonObject.put("operationType", "OUTCOME")
        } else {
            jsonObject.put("operationType", "INCOME")
        }
        try {
            jsonArray.put(jsonObject)
        } catch (jsonException: JSONException) {
            println(jsonException.localizedMessage)
        }
    }
}
