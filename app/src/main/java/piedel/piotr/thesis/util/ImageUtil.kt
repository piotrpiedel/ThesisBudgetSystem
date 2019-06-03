package piedel.piotr.thesis.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView


fun imageViewToBitmap(imageView: ImageView): Bitmap {
    return (imageView.drawable as BitmapDrawable).bitmap
}


