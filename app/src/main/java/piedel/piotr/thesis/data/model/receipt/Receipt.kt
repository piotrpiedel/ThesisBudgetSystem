package piedel.piotr.thesis.data.model.receipt

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import piedel.piotr.thesis.util.readDate
import piedel.piotr.thesis.util.writeDate
import java.util.*

@Entity(tableName = "receipt_table")
data class Receipt(var receiptImageSourcePath: String, var title: String, var date: Date, var value: Double) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


    @Ignore
    constructor() : this("Empty Uri", "Empty Title", Date(), 999.00)

    constructor(parcel: Parcel) : this(
            parcel.readString() as String,
            parcel.readString() as String,
            parcel.readDate() as Date,
            parcel.readDouble()) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(receiptImageSourcePath)
        parcel.writeString(title)
        parcel.writeDate(date)
        parcel.writeDouble(value)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Receipt> {
        override fun createFromParcel(parcel: Parcel): Receipt {
            return Receipt(parcel)
        }

        override fun newArray(size: Int): Array<Receipt?> {
            return arrayOfNulls(size)
        }
    }


}