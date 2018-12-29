package piedel.piotr.thesis.ui.fragment.receipt.receiptpreview

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.data.model.receipt.ReceiptRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.requestGlideBuilderOptionsAsBitmap
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class ReceiptPreviewPresenter @Inject constructor(private val receiptRepository: ReceiptRepository) : BasePresenter<ReceiptPreviewView>() {

    fun initFragment(receipt: Receipt?, requireContext: Context) {
        checkViewAttached()
        getImageForReceipt(receipt, requireContext)
        view?.fillTheReceiptPreviewFragmentWithData(receipt)

    }

    private fun getImageForReceipt(receipt: Receipt?, context: Context) {
        if (!receipt?.receiptImageSourcePath.equals("Empty Uri"))
            requestGlideBuilderOptionsAsBitmap(context, receipt?.receiptImageSourcePath)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            Timber.d("loadPictureFromGallery onLoadFailed")
                            return true
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                            Timber.d("loadPictureFromGallery onResourceReady")
                            view?.setImageViewWithBitmap(resource)
                            return true
                        }
                    })
                    .submit()
    }

}