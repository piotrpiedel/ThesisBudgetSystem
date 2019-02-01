package piedel.piotr.thesis.ui.fragment.importexport.importfromhtml

import com.squareup.moshi.*
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.util.simpleDateYearMonthDay
import java.io.IOException
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

class OperationClassAdapter() : JsonAdapter<Operation>() {
    private val options: JsonReader.Options = JsonReader.Options.of("value", "title", "operationType", "date")

    private val moshi = Moshi.Builder()
            .add(Date::class.java, CustomDateFormatAdapter().nullSafe())
            .add(Double::class.java, CustomDoubleFormatAdapter().nullSafe())
            .build()

    private val doubleAdapter: JsonAdapter<Double> = moshi.adapter(Double::class.java).nonNull()

    private var dateAdapter: JsonAdapter<Date> = moshi.adapter(Date::class.java)

    private val stringAdapter: JsonAdapter<String> = moshi.adapter(String::class.java)

    private val operationTypeAdapter: JsonAdapter<OperationType> = moshi.adapter(OperationType::class.java).nonNull()


    override fun toString(): String = "GeneratedJsonAdapter(Operation)"

    override fun fromJson(reader: JsonReader): Operation {
        var value: Double? = null
        var title: String? = null
        var operationType: OperationType? = null
        var date: Date? = null
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> value = doubleAdapter.fromJson(reader) ?: throw JsonDataException("Non-null value 'value' was null at ${reader.path}")
                1 -> title = stringAdapter.fromJson(reader) ?: throw JsonDataException("Non-null value 'title' was null at ${reader.path}")
                2 -> operationType = operationTypeAdapter.fromJson(reader) ?: throw JsonDataException("Non-null value 'operationType' was null at ${reader.path}")
                3 -> date = dateAdapter.fromJson(reader) ?: throw JsonDataException("Non-null value 'date' was null at ${reader.path}")
                -1 -> {
                    // Unknown name, skip it.
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        return Operation(value as Double, title, operationType as OperationType, date)
    }

    override fun toJson(writer: JsonWriter, value: Operation?) {
    }
}

class CustomDateFormatAdapter : JsonAdapter<Date>() {
    private var dateFormatForJson: DateFormat = simpleDateYearMonthDay()

    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Date {
        val string = reader.nextString()
        return dateFormatForJson.parse(string)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Date?) {
        val string = dateFormatForJson.format(value)
        writer.value(string)
    }
}

class CustomDoubleFormatAdapter : JsonAdapter<Double>() {

    private val doubleFormat: NumberFormat = NumberFormat.getInstance(Locale.FRANCE)

    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Double {
        val string = reader.nextString()
        return doubleFormat.parse(string).toDouble()
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Double?) {
        val string = doubleFormat.format(value)
        writer.value(string)
    }
}