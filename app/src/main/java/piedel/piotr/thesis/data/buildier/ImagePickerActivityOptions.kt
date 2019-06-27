package piedel.piotr.thesis.data.buildier

import android.os.Parcel
import android.os.Parcelable

data class ImagePickerActivityOptions(val aspectRatio_X: Float = 1.0F,
                                      val aspectRatio_Y: Float = 1.0F,
                                      val imageCompression: Int = 80,
                                      val lockAspectRatio: Boolean = false,
                                      val setBitmapMaxWidthHeight: Boolean = false,
                                      val bitmapMaxWidth: Int = 1000,
                                      val bitmapMaxHeight: Int = 1000) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(aspectRatio_X)
        parcel.writeFloat(aspectRatio_Y)
        parcel.writeInt(imageCompression)
        parcel.writeByte(if (lockAspectRatio) 1 else 0)
        parcel.writeByte(if (setBitmapMaxWidthHeight) 1 else 0)
        parcel.writeInt(bitmapMaxWidth)
        parcel.writeInt(bitmapMaxHeight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImagePickerActivityOptions> {
        override fun createFromParcel(parcel: Parcel): ImagePickerActivityOptions {
            return ImagePickerActivityOptions(parcel)
        }

        override fun newArray(size: Int): Array<ImagePickerActivityOptions?> {
            return arrayOfNulls(size)
        }
    }


}