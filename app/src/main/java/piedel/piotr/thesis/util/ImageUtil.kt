package piedel.piotr.thesis.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.widget.Toast
import piedel.piotr.thesis.exception.ProcessorException
import piedel.piotr.thesis.util.imageprocessor.ProcessorInterface
import piedel.piotr.thesis.util.imageprocessor.ProcessorInterface.ProcessorType.BINARIZE
import piedel.piotr.thesis.util.imageprocessor.ProcessorInterface.ProcessorType.GRAYSCALE
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


fun imageViewToBitmap(imageView: ImageView): Bitmap {
    return (imageView.drawable as BitmapDrawable).bitmap
}

fun Bitmap.bitmapToGrayScale(): Bitmap {
    val width: Int = this.width
    val height: Int = this.height
    val bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val c = Canvas(bmpGrayScale)
    val paint = Paint()
    val cm = ColorMatrix()
    cm.setSaturation(0f)
    val f = ColorMatrixColorFilter(cm)
    paint.colorFilter = f
    c.drawBitmap(this, 0F, 0F, paint)
    return bmpGrayScale
}

// context may be activity, baseApplicationContext or Fragment
fun Bitmap.toGrayScaleUsingRenderScript(context: Context): Bitmap {
    val processorInterface = ProcessorInterface.getProcessorInterfaceInstance(context)
    try {
        Timber.d("bitmap got grayscaled using Image Util")
        return processorInterface.processBitmapWithImageProcessor(this, GRAYSCALE)
    } catch (e: ProcessorException) {
        e.printStackTrace()
        Timber.e(e, "toGrayScaleUsingRenderScript")
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        return this
    }
}

// context may be activity, baseApplicationContext or Fragment
fun Bitmap.binarizeBitmapUsingRenderScriptTempFunctionName(context: Context): Bitmap {
    val processorInterface = ProcessorInterface.getProcessorInterfaceInstance(context)
    try {
        Timber.d("bitmap got binarized using Image Util")
        return processorInterface.processBitmapWithImageProcessor(this, BINARIZE)
    } catch (e: ProcessorException) {
        e.printStackTrace()
        Timber.e(e, "binarizeBitmapUsingRenderScriptTempFunctionName")
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        return this
    }
}

private fun loadImageFromStorage(path: String) {
    try {
        val f = File(path, "profile.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}


fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

//fun decodeSampledBitmapFromResource(reqWidth: Int, reqHeight: Int): Bitmap {
//    // First decode with inJustDecodeBounds=true to check dimensions
//    return BitmapFactory.Options().run {
//        inJustDecodeBounds = true
////        BitmapFactory.decodeResource(res, resId, this)
////        BitmapFactory.decodeStream(FileInputStream(originalPickedImageFile))
//
//        // Calculate inSampleSize
//        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
//
//        // Decode bitmap with inSampleSize set
//        inJustDecodeBounds = false
//
//        BitmapFactory.decodeResource(res, resId, this)
//    }
//}