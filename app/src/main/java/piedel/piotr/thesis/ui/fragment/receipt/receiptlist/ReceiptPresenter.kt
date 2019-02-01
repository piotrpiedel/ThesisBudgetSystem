package piedel.piotr.thesis.ui.fragment.receipt.receiptlist

import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.data.model.receipt.ReceiptRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class ReceiptPresenter @Inject constructor(private val receiptRepository: ReceiptRepository) : BasePresenter<ReceiptView>() {

    private var disposable: Disposable? = null


    fun initFragment() {
        checkViewAttached()
        view?.showProgressBar(true)
        view?.setReceiptListRecyclerView()
        view?.setAdapter()
        loadReceiptList()
    }

    private fun loadReceiptList() {
        disposable = receiptRepository.selectAllReceipts()
                .subscribe({ receipts ->
                    view?.showProgressBar(false)
                    view?.updateList(receipts)
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

    fun onAddNewReceiptClicked() {
        view?.replaceWithAddReceiptFragment()
    }

    fun deleteActionReceipt(receipt: Receipt, itemPosition: Int) {
        receiptRepository.deleteReceipt(receipt)
                .subscribe(object : CompletableObserverMain() {
                    override fun onComplete() {
                        notifyAdapterItemRemoved(itemPosition)
                    }
                })
    }

    private fun notifyAdapterItemRemoved(itemPosition: Int) {
        view?.notifyItemRemoved(itemPosition)
    }

    fun deleteAllReceiptsAction() {
        receiptRepository.deleteAllReceipts()
                .subscribe(object : CompletableObserverMain() {
                    override fun onComplete() {
                        val receiptsList = mutableListOf<Receipt>()
                        view?.updateList(receiptsList)
                    }
                })
    }
}