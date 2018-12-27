package piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.ui.fragment.receipt.receiptadd.ReceiptAddFragment


class ChoosePictureSourceDialog : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_choose_picture_source, container)
        isCancelable = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        val title = arguments?.getString("title", "Choose receipt image source")
        dialog.setTitle(title)
    }

    fun onLoadFromGalleryClick(passedActivityFragment: FragmentActivity) {
        checkPermissionsForStorage(passedActivityFragment)
    }

    private fun checkPermissionsForStorage(fragmentActivity: FragmentActivity) {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(fragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission)) {
            } else {
                ActivityCompat.requestPermissions(fragmentActivity, arrayOf(permission), ReceiptAddFragment.PERMISSIONS_REQUEST_CODE)
            }
        } else {
            onInternalMemoryPermissionGranted()
        }
    }


    fun onLoadFromCameraClick(requireActivity: FragmentActivity) {
        checkPermissionsForCamera(requireActivity)
    }

    private fun checkPermissionsForCamera(fragmentActivity: FragmentActivity) {
        val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(fragmentActivity, permission.get(0)) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission[0])) {
            } else {
                ActivityCompat.requestPermissions(fragmentActivity, permission, ReceiptAddFragment.PERMISSIONS_REQUEST_CODE)
            }
        } else {
            onCameraPermissionGranted()
        }
    }

    @OnClick(R.id.receipt_input_button_add_receipt_from_camera)
    fun onLoadFromCameraButtonClick() {
        onLoadFromCameraClick(requireActivity())
    }

    private fun onCameraPermissionGranted() {
        targetFragment?.onActivityResult(
                targetRequestCode,
                Activity.RESULT_OK,
                Intent().putExtra(FRAGMENT_INTENT_CHOSEN_BUTTON, ImageSourceOptions.CAMERA)
        )
        dismiss()
    }

    @OnClick(R.id.receipt_input_button_add_receipt_from_gallery)
    fun onLoadFromGalleryButtonClick() {
        onLoadFromGalleryClick(requireActivity())
    }

    private fun onInternalMemoryPermissionGranted() {
        targetFragment?.onActivityResult(
                targetRequestCode,
                Activity.RESULT_OK,
                Intent().putExtra(FRAGMENT_INTENT_CHOSEN_BUTTON, ImageSourceOptions.INTERNAL_MEMORY)
        )
        dismiss()
    }

    companion object {

        const val FRAGMENT_TAG: String = "Choose Source Of Image"

        const val FRAGMENT_INTENT_CHOSEN_BUTTON = "buttonValue"

        const val FRAGMENT_TITLE: String = " Choose Source Of Image "

        fun newInstance(title: String): ChoosePictureSourceDialog {
            val frag = ChoosePictureSourceDialog()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }
}
