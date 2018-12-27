package piedel.piotr.thesis.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import piedel.piotr.thesis.R
import java.io.ByteArrayOutputStream

fun convertToBitmap(passedByteArray: ByteArray): Bitmap? {
    return BitmapFactory.decodeByteArray(passedByteArray, 0, passedByteArray.size)

}


fun imageViewToBitmap(imageView: ImageView): ByteArray {
    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}

fun convertToBitmapAndCompress(imageView: ImageView): ByteArray {
    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}

fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

fun decodeSampledBitmapFromByteArray(passedByteArray: ByteArray, reqWidth: Int, reqHeight: Int): Bitmap {
//    source:
//    https://stackoverflow.com/questions/477572/strange-out-of-memory-issue-while-loading-an-image-to-a-bitmap-object/823966#823966

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    // Calculate inSampleSize
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeByteArray(passedByteArray, 0, passedByteArray.size, options)

    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeByteArray(passedByteArray, 0, passedByteArray.size, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}


@SuppressLint("CheckResult")
fun requestGlideBuildierOptionsAsBitmap(context: Context, picturePath: String?): RequestBuilder<Bitmap> {
    val requestOptions = RequestOptions()
    requestOptions.placeholder(getCircularProgressDrawable(context))
    requestOptions.error(R.drawable.ic_outline_error_outline)
    requestOptions.override(200, 200)
    return Glide.with(context)
            .asBitmap()
            .load(picturePath)
            .apply(requestOptions)
}