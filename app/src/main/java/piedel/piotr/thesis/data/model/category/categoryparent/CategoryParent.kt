package piedel.piotr.thesis.data.model.category.categoryparent

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "category_parent_table", indices = [Index(value = ["categoryId"], unique = true)])
data class CategoryParent(@PrimaryKey(autoGenerate = true) val categoryId: Int?, val category_title: String) : Parcelable {

    constructor(categoryTitle: String) : this(null, categoryTitle)

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString() as String) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(categoryId)
        parcel.writeString(category_title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryParent> {
        override fun createFromParcel(parcel: Parcel): CategoryParent {
            return CategoryParent(parcel)
        }

        override fun newArray(size: Int): Array<CategoryParent?> {
            return arrayOfNulls(size)
        }
    }

}