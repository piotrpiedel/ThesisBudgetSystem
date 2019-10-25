package piedel.piotr.thesis.data.model.operation

import android.os.Parcel
import android.os.Parcelable

data class OperationSelectable(
        val operation: Operation,
        var selected: Boolean = false) : Parcelable, OperationBase by operation {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable<Operation>(Operation::class.java.classLoader) as Operation,
            parcel.readByte() != 0.toByte()) {
    }

    fun isSelected(): Boolean {
        return selected
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(operation, flags)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OperationSelectable> {
        override fun createFromParcel(parcel: Parcel): OperationSelectable {
            return OperationSelectable(parcel)
        }

        override fun newArray(size: Int): Array<OperationSelectable?> {
            return arrayOfNulls(size)
        }
    }


}