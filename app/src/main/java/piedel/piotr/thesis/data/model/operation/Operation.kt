package piedel.piotr.thesis.data.model.operation

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.util.getRandomCategory
import piedel.piotr.thesis.util.readDate
import piedel.piotr.thesis.util.writeDate
import java.util.Date

@Entity(tableName = "operation_table",
        foreignKeys = arrayOf(ForeignKey(entity = CategoryChild::class,
                parentColumns = ["categoryId"],
                childColumns = ["other_category_id"]
        )))
data class Operation(var value: Double, var title: String?, var operationType: OperationType, var date: Date?, var other_category_id: Int?) : Parcelable { //null without categoryChild
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readString(),
            OperationType.valueOf(parcel.readString() as String),
            parcel.readDate(),
            parcel.readValue(Int::class.java.classLoader) as? Int) {
        id = parcel.readInt()
    }

    @Ignore
    constructor(value: Double, title: String?, operationType: OperationType, date: Date?) : this(value, title, operationType, date, getRandomCategory())


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

data class OperationCategoryTuple(@Embedded var operation: Operation, @Embedded var categoryChild: CategoryChild?)

//data class DateValueTuple(var date: Date, var sumValueForDate: Double)

data class DateValueTuple(var date: Date, var sumValueForDate: Double)

data class DateValueCategoryTuple(var date: Date, var sumValueForCategory: Double, var categoryId: Int, var category_title: String)
