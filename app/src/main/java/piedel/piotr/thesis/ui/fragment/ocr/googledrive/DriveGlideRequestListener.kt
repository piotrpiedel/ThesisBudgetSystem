package piedel.piotr.thesis.ui.fragment.ocr.googledrive

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import timber.log.Timber

class DriveGlideRequestListener(private val context: Context, private val view: ImportFromImageDriveContract.ImportFromImageDriveView?) : RequestListener<Bitmap> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
        Timber.d("loadPictureFromGallery onLoadFailed exception: %s", e.toString())
        Timber.d("model  %s", model.toString())
        Timber.d("target  %s", target.toString())
        Timber.d("isFirstResource  %s", isFirstResource.toString())
        return true
    }

    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        (context as FragmentActivity).runOnUiThread { view?.setImageViewWithBitmap(resource) }
        return true
    }
}