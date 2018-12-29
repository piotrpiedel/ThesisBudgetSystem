package piedel.piotr.thesis.ui.fragment.receipt.receiptpreview

import android.graphics.Bitmap
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseView

interface ReceiptPreviewView : BaseView {

    fun setImageViewWithBitmap(resource: Bitmap)

    fun fillTheReceiptPreviewFragmentWithData(receipt: Receipt?)
}