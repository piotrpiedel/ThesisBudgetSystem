package piedel.piotr.thesis.data.model.operation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.util.readDate
import piedel.piotr.thesis.util.writeDate
import java.util.*

@Entity(tableName = "operation_table",
        foreignKeys = arrayOf(ForeignKey(entity = Category::class,
                parentColumns = ["categoryId"],
                childColumns = ["other_category_id"]
        )))
data class Operation(var value: Double, var title: String?, var operationType: OperationType, var date: Date?, var other_category_id: Int? = null) : Parcelable { //null without category
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readString(),
            OperationType.valueOf(parcel.readString()),
            parcel.readDate(),
            parcel.readValue(Int::class.java.classLoader) as? Int) {
        id = parcel.readInt()
    }

    @Ignore
    constructor(value: Double, title: String?, operationType: OperationType, date: Date?)
            : this(value, title, operationType, date, null)

    @Ignore
    constructor() : this(123456.1, "Empty Constructor", OperationType.OUTCOME, Date())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(value)
        parcel.writeString(title)
        parcel.writeString(operationType.name)
        parcel.writeDate(date)
        parcel.writeValue(other_category_id)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Operation> {
        override fun createFromParcel(parcel: Parcel): Operation {
            return Operation(parcel)
        }

        override fun newArray(size: Int): Array<Operation?> {
            return arrayOfNulls(size)
        }
    }
}

data class OperationValueOperationType(var value: Double, var operationType: OperationType)