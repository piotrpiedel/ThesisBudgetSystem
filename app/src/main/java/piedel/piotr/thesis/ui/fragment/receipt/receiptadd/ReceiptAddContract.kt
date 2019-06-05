package piedel.piotr.thesis.ui.fragment.receipt.receiptadd

import android.content.Intent
import android.graphics.Bitmap
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter
import piedel.piotr.thesis.util.listener.CameraAndStoragePermissionListener

interface ReceiptAddContract {
    interface ReceiptAddView : BaseView, CameraAndStoragePermissionListener.CameraAndStorageViewInterface {

        fun showError()

        fun setOnCalendarClickListener()

        fun setReceiptImageFromResource(bitmapImage: Bitmap)

        fun startCreatingReceipt()

        fun createReceipt(receiptId: Long, receipt: Receipt)

        fun returnFromFragment()

        fun showProgressBar(show: Boolean)

        fun enableSaveButton(isEnabled: Boolean)

        fun checkPermissions()

    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initFragment(receipt: Receipt?)

        fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?, passedActivity: FragmentActivity)

        fun loadPictureFromGallery(fragmentActivity: FragmentActivity, picturePath: String?)

        fun onSaveOperationButtonClicked()

        fun updateReceipt(receipt: Receipt)

        fun generateEmptyReceipt()

        fun observeTheInputValue(titleEditText: EditText, dateTextView: TextView, valueEditText: EditText)

        fun checkPermissionsForStorageAndCamera(passedActivityFragment: FragmentActivity)
    }
}