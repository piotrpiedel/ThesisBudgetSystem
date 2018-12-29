package piedel.piotr.thesis.data.model.category

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "category_table", indices = [Index(value = ["categoryId"], unique = true)])
data class Category(@PrimaryKey(autoGenerate = true) val categoryId: Int?, val category_title: String, var parentCategoryId: Int?) : Parcelable {

    constructor(categoryTitle: String, parentCategoryIdSec: Int?) : this(null, categoryTitle, parentCategoryIdSec)

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        categoryId?.let { parcel.writeInt(it) }
        parcel.writeString(category_title)
        parcel.writeValue(parentCategoryId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }


}