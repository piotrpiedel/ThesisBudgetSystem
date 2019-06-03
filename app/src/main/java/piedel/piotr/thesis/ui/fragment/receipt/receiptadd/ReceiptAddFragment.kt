package piedel.piotr.thesis.ui.fragment.receipt.receiptadd

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.configuration.choosePictureSourceDialogRequestCode
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog.ChoosePictureSourceDialog
import piedel.piotr.thesis.util.dateFromStringNullCheck
import piedel.piotr.thesis.util.saveImageFile
import piedel.piotr.thesis.util.simpleDateFormatDayMonthYear
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class ReceiptAddFragment : BaseFragment(), ReceiptAddContract.ReceiptAddView {

    @Inject
    lateinit var receiptAddPresenter: ReceiptAddPresenter

    @BindView(R.id.receipt_picture)
    lateinit var receiptPicture: ImageView

    @BindView(R.id.receipt_calendar_container)
    lateinit var calendarContainer: LinearLayout

    @BindView(R.id.receipt_title)
    lateinit var receiptTitle: EditText

    @BindView(R.id.receipt_input_date)
    lateinit var receiptDate: TextView

    @BindView(R.id.receipt_value)
    lateinit var valueInput: EditText

    @BindView(R.id.receipt_button_save)
    lateinit var buttonSave: Button

    private var receipt: Receipt? = null

    override val layout: Int
        get() = R.layout.fragment_receipt_add

    override val toolbarTitle: String
        get() = context?.getString(R.string.add_receipt_title).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        receiptAddPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receipt = arguments?.getParcelable(RECEIPT_ADD_KEY)
        initInputObservers()
        receiptAddPresenter.initChooseDialog()
    }

    private fun initInputObservers() {
        receiptAddPresenter.observeTheInputValue(receiptTitle, receiptDate, valueInput)
    }

    override fun enableSaveButton(isEnabled: Boolean) {
        buttonSave.isEnabled = isEnabled
    }

    override fun showChooseDialog() {
        val choosePictureSourceDialog = ChoosePictureSourceDialog()
        choosePictureSourceDialog.setTargetFragment(this, choosePictureSourceDialogRequestCode)
        choosePictureSourceDialog.show(fragmentManager, ChoosePictureSourceDialog.FRAGMENT_TAG)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        receiptAddPresenter.handleOnActivityResult(requestCode, resultCode, data, requireActivity())
    }

    override fun setOnCalendarClickListener() {
        val calendar = Calendar.getInstance()
        val dateSetListener = onDateSetListener(calendar)
        setOnDateClickListener(dateSetListener, calendar)
    }

    private fun setOnDateClickListener(dateSetListener: DatePickerDialog.OnDateSetListener, cal: Calendar) {
        calendarContainer.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                    cal.get(Calendar.YEAR), //the initially selected year
                    cal.get(Calendar.MONTH), //the initially selected month
                    cal.get(Calendar.DAY_OF_MONTH)) //the initially selected day of month
                    .show()
        }
    }

    private fun onDateSetListener(cal: Calendar): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { _, years, monthOfYear, dayOfMonth ->
            setSelectedDate(cal, years, monthOfYear, dayOfMonth)
            receiptDate.text = simpleDateFormatDayMonthYear().format(cal.time)
        }
    }

    private fun setSelectedDate(cal: Calendar, years: Int, monthOfYear: Int, dayOfMonth: Int) {
        cal.set(Calendar.YEAR, years)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    @OnClick(R.id.receipt_button_save)
    fun onSaveButtonClick() {
        receiptAddPresenter.onSaveOperationButtonClicked()
    }

    override fun setReceiptImageFromResource(bitmapImage: Bitmap) {
        requireActivity().runOnUiThread {
            receiptPicture.setImageBitmap(bitmapImage)
        }

    }

    //for autogenerating purposes
    override fun startCreatingReceipt() {
        receiptAddPresenter.generateEmptyReceipt()
    }

    override fun createReceipt(receiptId: Long, receipt: Receipt) {
        Timber.d(receipt.id.toString())
        receipt.apply {
            receiptImageSourcePath = saveImageFile((receiptPicture.drawable as BitmapDrawable).bitmap, receipt)
            title = receiptTitle.text.toString().trim()
            date = dateFromStringNullCheck(receiptDate.text.toString())
            value = valueInput.text.toString().trim().toDouble()
        }
        receiptAddPresenter.updateReceipt(receipt)
    }

    override fun returnFromFragment() {
        getMainActivity().showProgressBar(false)
        activity?.onBackPressed()
    }

    override fun showProgressBar(show: Boolean) {
        getMainActivity().showProgressBar(true)
    }

    override fun showError() {
        Toast.makeText(context, getString(R.string.error_occurred_try_something_else), Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val RECEIPT_ADD_KEY: String = "RECEIPT_ADD_KEY"
        const val FRAGMENT_TAG: String = "ReceiptAddFragment"


        fun newInstance(receipt: Receipt): ReceiptAddFragment {
            val addReceiptFragment = ReceiptAddFragment()
            val args = Bundle()
            args.putParcelable(RECEIPT_ADD_KEY, receipt)
            addReceiptFragment.arguments = args
            return addReceiptFragment
        }
    }
}