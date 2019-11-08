package piedel.piotr.thesis.ui.fragment.receipt.receiptadd

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.dateFromStringNullCheck
import piedel.piotr.thesis.util.dateToDayMonthYearFormatString
import piedel.piotr.thesis.util.getImageFilePicker
import piedel.piotr.thesis.util.saveImageFile
import piedel.piotr.thesis.util.showToast
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class ReceiptAddNewFragment : BaseFragment(), ReceiptAddNewContract.ReceiptAddView {

    @Inject
    lateinit var receiptAddNewPresenter: ReceiptAddNewPresenter

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
        receiptAddNewPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receipt = arguments?.getParcelable(RECEIPT_ADD_KEY)
        initInputObservers()
        receiptAddNewPresenter.initFragment(receipt)
    }

    private fun initInputObservers() {
        receiptAddNewPresenter.observeTheInputValue(receiptTitle, receiptDate, valueInput)
    }

    override fun checkPermissions() {
        receiptAddNewPresenter.checkPermissionsForStorageAndCamera(requireActivity())
    }


    override fun enableSaveButton(isEnabled: Boolean) {
        buttonSave.isEnabled = isEnabled
    }

    override fun showFileChooserGalleryAndCamera() {
        getImageFilePicker(context, true)
    }

    override fun onPermissionPermanentlyDenied() {
        showToast(requireContext(), getString(R.string.the_permission_is_denied_permanently))
        activity?.onBackPressed()
    }

    override fun showToastWithRequestOfPermissions() {
        showToast(requireContext(), getString(R.string.permission_required_storage_camera_optional))
        activity?.onBackPressed()
    }

    override fun showFileChooserOnlyGallery() {
        getImageFilePicker(context, false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        receiptAddNewPresenter.handleOnActivityResult(requestCode, resultCode, data, requireActivity())
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
            receiptDate.text = dateToDayMonthYearFormatString(cal.time)
        }
    }

    private fun setSelectedDate(cal: Calendar, years: Int, monthOfYear: Int, dayOfMonth: Int) {
        cal.set(Calendar.YEAR, years)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    @OnClick(R.id.receipt_button_save)
    fun onSaveButtonClick() {
        receiptAddNewPresenter.onSaveOperationButtonClicked()
    }

    override fun setReceiptImageFromResource(bitmapImage: Bitmap) {
        requireActivity().runOnUiThread {
            receiptPicture.setImageBitmap(bitmapImage)
        }
    }

    //for autogeneration purposes
    override fun startCreatingReceipt() {
        receiptAddNewPresenter.generateEmptyReceipt()
    }

    override fun createReceipt(receiptId: Long, receipt: Receipt) {
        Timber.d(receipt.id.toString())
        receipt.apply {
            receiptImageSourcePath = saveImageFile((receiptPicture.drawable as BitmapDrawable).bitmap, receipt)
            title = receiptTitle.text.toString().trim()
            date = dateFromStringNullCheck(receiptDate.text.toString())
            value = valueInput.text.toString().trim().toDouble()
        }
        receiptAddNewPresenter.updateReceipt(receipt)
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
        const val FRAGMENT_TAG: String = "ReceiptAddNewFragment"


        fun newInstance(receipt: Receipt): ReceiptAddNewFragment {
            val addReceiptFragment = ReceiptAddNewFragment()
            val args = Bundle()
            args.putParcelable(RECEIPT_ADD_KEY, receipt)
            addReceiptFragment.arguments = args
            return addReceiptFragment
        }
    }
}