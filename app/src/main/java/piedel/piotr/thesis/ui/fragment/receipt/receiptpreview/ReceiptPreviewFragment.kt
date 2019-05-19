package piedel.piotr.thesis.ui.fragment.receipt.receiptpreview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.stfalcon.imageviewer.StfalconImageViewer
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.GlideApp
import piedel.piotr.thesis.util.dateToDayMonthYearFormatString
import piedel.piotr.thesis.util.imageViewToBitmap
import javax.inject.Inject

class ReceiptPreviewFragment : BaseFragment(), ReceiptPreviewContract.ReceiptPreviewView {

    @Inject
    lateinit var receiptPreviewPresenter: ReceiptPreviewPresenter

    @BindView(R.id.receipt_picture)
    lateinit var receiptPicture: ImageView

    @BindView(R.id.receipt_input_date)
    lateinit var receiptDate: TextView

    @BindView(R.id.receipt_title)
    lateinit var receiptTitle: TextView

    @BindView(R.id.receipt_value)
    lateinit var valueInput: TextView

    override val layout: Int
        get() = R.layout.fragment_receipt_preview

    override val toolbarTitle: String
        get() = context?.getString(R.string.receipt_preview).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        receiptPreviewPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity().showProgressBar(true)
        val receipt: Receipt? = arguments?.getParcelable(RECEIPT_PREVIEW_KEY)
        initFragment(receipt)
    }

    private fun initFragment(receipt: Receipt?) {
        receiptPreviewPresenter.initFragment(receipt, requireContext())
    }

    override fun setImageViewWithBitmap(resource: Bitmap) {
        receiptPicture.setImageBitmap(resource)
    }

    override fun fillTheReceiptPreviewFragmentWithData(receipt: Receipt?) {
        receipt?.apply {
            receiptDate.text = dateToDayMonthYearFormatString(date)
            receiptTitle.text = receipt.title
            valueInput.text = receipt.value.toString()
        }
        getMainActivity().showProgressBar(false)
    }

    @OnClick(R.id.receipt_picture)
    fun showImageViewFullScreen() {
        StfalconImageViewer.Builder<Bitmap>(context, arrayOf(imageViewToBitmap(receiptPicture))) { view, image ->
            GlideApp.with(requireContext()).load(image).into(view)
        }.show()
    }

    companion object {

        private const val RECEIPT_PREVIEW_KEY: String = "RECEIPT_PREVIEW_KEY"

        const val FRAGMENT_TAG: String = "ReceiptPreviewFragment"


        fun newInstance(receipt: Receipt): ReceiptPreviewFragment {
            val addReceiptFragment = ReceiptPreviewFragment()
            val args = Bundle()
            args.putParcelable(RECEIPT_PREVIEW_KEY, receipt)
            addReceiptFragment.arguments = args
            return addReceiptFragment
        }
    }


}