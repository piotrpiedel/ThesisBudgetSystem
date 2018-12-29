package piedel.piotr.thesis.ui.fragment.receipt.receiptadd

import android.graphics.Bitmap
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseView

interface ReceiptAddView : BaseView {

    fun showError()

    fun setOnCalendarClickListener()

    fun setReceiptImageFromResource(bitmapImage: Bitmap)

    fun startCreatingReceipt()

    fun createReceipt(receiptId: Long, receipt: Receipt)

    fun returnFromFragment()

    fun showProgressBar(show: Boolean)

    fun showChooseDialog()

    fun enableSaveButton(isEnabled: Boolean)

}