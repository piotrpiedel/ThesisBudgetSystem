package piedel.piotr.thesis.ui.fragment.receipt.receiptpreview

import android.content.Context
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.data.model.receipt.ReceiptRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.receipt.receiptpreview.ReceiptPreviewContract.PresenterContract
import piedel.piotr.thesis.ui.fragment.receipt.receiptpreview.ReceiptPreviewContract.ReceiptPreviewView
import piedel.piotr.thesis.util.glide.glideLoadAsBitmap
import javax.inject.Inject

@ConfigPersistent
class ReceiptPreviewPresenter @Inject constructor(private val receiptRepository: ReceiptRepository) : BasePresenter<ReceiptPreviewView>(), PresenterContract<ReceiptPreviewView> {

    override fun initFragment(receipt: Receipt?, requireContext: Context) {
        checkViewAttached()
        getImageForReceipt(receipt, requireContext)
        view?.fillTheReceiptPreviewFragmentWithData(receipt)

    }

    private fun getImageForReceipt(receipt: Receipt?, context: Context) {
        if (!receipt?.receiptImageSourcePath.equals("Empty Uri"))
            glideLoadAsBitmap(context, receipt?.receiptImageSourcePath)
                    .listener(ReceiptPreviewGlideListener(context, view))
                    .submit()
    }

}