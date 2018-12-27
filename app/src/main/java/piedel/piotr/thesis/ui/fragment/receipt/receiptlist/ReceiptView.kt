package piedel.piotr.thesis.ui.fragment.receipt.receiptlist

import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseView

interface ReceiptView : BaseView {

    fun setReceiptListRecyclerView()

    fun setAdapter()

    fun replaceWithAddReceiptFragment()

    fun showError(throwable: Throwable?)

    fun updateList(receipts: MutableList<Receipt>)

    fun showProgressBar(show: Boolean)
}