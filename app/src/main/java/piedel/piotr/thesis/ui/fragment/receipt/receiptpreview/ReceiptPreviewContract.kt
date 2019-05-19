package piedel.piotr.thesis.ui.fragment.receipt.receiptpreview

import android.content.Context
import android.graphics.Bitmap
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface ReceiptPreviewContract {
    interface ReceiptPreviewView : BaseView {

        fun setImageViewWithBitmap(resource: Bitmap)

        fun fillTheReceiptPreviewFragmentWithData(receipt: Receipt?)
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initFragment(receipt: Receipt?, requireContext: Context)
    }
}