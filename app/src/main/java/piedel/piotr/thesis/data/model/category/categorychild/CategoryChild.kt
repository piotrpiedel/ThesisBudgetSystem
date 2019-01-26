package piedel.piotr.thesis.data.model.category.categorychild

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import piedel.piotr.thesis.data.model.category.categoryparent.CategoryParent

@Entity(tableName = "category_child_table", indices = [Index(value = ["categoryId"], unique = true)])
data class CategoryChild(@PrimaryKey(autoGenerate = true) val categoryId: Int?, val category_title: String, var parentCategoryId: Int?) : Parcelable {

    constructor(categoryTitle: String, parentCategoryIdSec: Int?) : this(null, categoryTitle, parentCategoryIdSec)

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() as String,
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

    companion object CREATOR : Parcelable.Creator<CategoryChild> {
        override fun createFromParcel(parcel: Parcel): CategoryChild {
            return CategoryChild(parcel)
        }

        override fun newArray(size: Int): Array<CategoryChild?> {
            return arrayOfNulls(size)
        }
    }
}

data class CategoryChildParentTuple(@Embedded var categoryChild: CategoryChild, @Embedded var categoryParent: CategoryParent, var countParent: Int)
