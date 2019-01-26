package piedel.piotr.thesis.data.model.category.categoryparent

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "category_parent_table", indices = [Index(value = ["category_id_parent"], unique = true)])
data class CategoryParent(@PrimaryKey(autoGenerate = true) val category_id_parent: Int, val category_title_parent: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() as String) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(category_id_parent)
        parcel.writeString(category_title_parent)
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