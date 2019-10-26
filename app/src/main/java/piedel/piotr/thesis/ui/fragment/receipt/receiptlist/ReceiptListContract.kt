package piedel.piotr.thesis.ui.fragment.receipt.receiptlist

import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface ReceiptListContract {
    interface ReceiptView : BaseView {

        fun setReceiptListRecyclerView()

        fun setAdapter()

        fun replaceWithAddReceiptFragment()

        fun showError(throwable: Throwable?)

        fun updateList(receipts: MutableList<Receipt>)

        fun showProgressBar(show: Boolean)

        fun notifyItemRemoved(itemPosition: Int)
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initFragment()

        fun onAddNewReceiptClicked()

        fun deleteActionReceipt(receipt: Receipt, itemPosition: Int)

        fun deleteAllReceiptsAction()
    }

}