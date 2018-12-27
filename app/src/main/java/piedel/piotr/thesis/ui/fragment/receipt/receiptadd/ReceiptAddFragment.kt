package piedel.piotr.thesis.ui.fragment.receipt.receiptadd

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import io.reactivex.annotations.NonNull
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog.ChoosePictureSourceDialog
import piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog.ImageSourceOptions
import piedel.piotr.thesis.util.choosePictureSourceDialogRequestCode
import piedel.piotr.thesis.util.dateFromStringNullCheck
import piedel.piotr.thesis.util.saveImageFile
import piedel.piotr.thesis.util.simpleDateFormat
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class ReceiptAddFragment : BaseFragment(), ReceiptAddView {

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

    @BindView(R.id.value_container)
    lateinit var valueContainer: LinearLayout

    private var receipt: Receipt? = null

    override val layout: Int
        get() = R.layout.fragment_receipt_add

    override val toolbarTitle: String
        get() = FRAGMENT_TITLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        receiptAddPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receipt = arguments?.getParcelable(RECEIPT_ADD_KEY)
        receiptAddPresenter.initChooseDialog()
    }

    override fun showChooseDialog() {
        val choosePictureSourceDialog = ChoosePictureSourceDialog()
        choosePictureSourceDialog.setTargetFragment(this, choosePictureSourceDialogRequestCode)
        choosePictureSourceDialog.show(fragmentManager, ChoosePictureSourceDialog.FRAGMENT_TAG)
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
            receiptDate.text = simpleDateFormat().format(cal.time)
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

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        receiptAddPresenter.resultFromRequestPermission(requestCode, grantResults)
    }


    //TODO: need huge refactor- all permissions - if free time
    override fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a picture to load"), FILE_SELECT_REQUEST_CODE)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, ex.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST_CODE);
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "Please install a camera", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, ex.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_SELECT_REQUEST_CODE) {
            receiptAddPresenter.getFilePathFromResult(requestCode, resultCode, data, requireActivity())
        } else if (requestCode == CAMERA_PIC_REQUEST_CODE) {
            receiptAddPresenter.getPictureFromCamera(requestCode, resultCode, data)
        } else if (requestCode == choosePictureSourceDialogRequestCode && resultCode == Activity.RESULT_OK) {
            val choosePictureSourceDialog = data?.getSerializableExtra(ChoosePictureSourceDialog.FRAGMENT_INTENT_CHOSEN_BUTTON) as ImageSourceOptions
            receiptAddPresenter.switchCameraMemorySource(choosePictureSourceDialog, requireActivity())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setReceiptImageFromResource(bitmapImage: Bitmap) {
        receiptPicture.setImageBitmap(bitmapImage)
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
            value = (valueContainer.getChildAt(0) as EditText).text.toString().trim().toDouble()
        }
        receiptAddPresenter.updateReceipt(receipt)
    }

    override fun returnFromFragment() {
        getMainActivity().showProgress(false)
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    override fun showProgressBar(show: Boolean) {
        getMainActivity().showProgress(true)
    }

    override fun showError() {
        Toast.makeText(context, "Error occurred  - try something else", Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val RECEIPT_ADD_KEY: String = "RECEIPT_ADD_KEY"
        const val FRAGMENT_TAG: String = "ReceiptAddFragment"
        const val FRAGMENT_TITLE: String = "Add receipt"

        const val PERMISSIONS_REQUEST_CODE = 90
        const val FILE_SELECT_REQUEST_CODE = 91

        val CAMERA_PIC_REQUEST_CODE = 92

        fun newInstance(receipt: Receipt): ReceiptAddFragment {
            val addReceiptFragment = ReceiptAddFragment()
            val args = Bundle()
            args.putParcelable(RECEIPT_ADD_KEY, receipt)
            addReceiptFragment.arguments = args
            return addReceiptFragment
        }
    }
}