package piedel.piotr.thesis.data.model.converters

import android.arch.persistence.room.TypeConverter
import piedel.piotr.thesis.data.model.operation.OperationType

class OperationTypeConverter {

    @TypeConverter
    fun fromEnum(value: OperationType?): String? {
        return value?.name
    }

    @TypeConverter
    fun fromString(value: String?): OperationType? {
        return value?.let { OperationType.valueOf(it) }
    }

}

