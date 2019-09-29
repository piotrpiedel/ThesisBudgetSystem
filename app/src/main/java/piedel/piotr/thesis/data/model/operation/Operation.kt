package piedel.piotr.thesis.data.model.operation

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.util.getRandomCategory
import piedel.piotr.thesis.util.readDate
import java.util.*

@Entity(tableName = "operation_table",
        foreignKeys = [ForeignKey(entity = CategoryChild::class,
                parentColumns = ["categoryId"],
                childColumns = ["other_category_id"]
        )])
data class Operation(override var value: Double = 123456.1,
                     override var title: String? = "Empty Constructor",
                     override var operationType: OperationType = OperationType.OUTCOME,
                     override var date: Date? = Date(),
                     override var other_category_id: Int? = 1) : Parcelable, OperationBase { //null without category
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readString(),
            OperationType.valueOf(parcel.readString() as String),
            parcel.readDate(),
            parcel.readInt()) {
        id = parcel.readInt()
    }

    @Ignore
    constructor(title: String?) : this(123456.1, title)

    //this is for development
    @Ignore
    constructor(value: Double, title: String?, operationType: OperationType, date: Date?) : this(value, title, operationType, date, getRandomCategory())

//    @Ignore
//    constructor(value: Double, title: String?, operationType: OperationType, date: Date?, other_category_id: Int? = null) : this(value, title, operationType, date, other_category_id ?: 1)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(value)
        parcel.writeString(title)
        other_category_id?.let { parcel.writeInt(it) }
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

data class DateValueTuple(var date: Date, var sumValueForDate: Double)

data class DateValueCategoryTuple(var date: Date, var sumValueForCategory: Double, var category_id_parent: Int, var category_title_parent: String)
