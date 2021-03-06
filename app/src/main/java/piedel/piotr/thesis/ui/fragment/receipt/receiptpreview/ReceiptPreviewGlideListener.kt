package piedel.piotr.thesis.ui.fragment.receipt.receiptpreview

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import timber.log.Timber

class ReceiptPreviewGlideListener(private val context: Context, private val view: ReceiptPreviewContract.ReceiptPreviewView?) : RequestListener<Bitmap> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
        Timber.d("loadPictureFromGallery onLoadFailed")
        return true
    }

    override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        (context as FragmentActivity).runOnUiThread { view?.setImageViewWithBitmap(resource) }
        return true
    }
}