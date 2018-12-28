package piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.util.CAMERA_PIC_REQUEST_CODE
import piedel.piotr.thesis.util.FILE_SELECT_REQUEST_CODE
import piedel.piotr.thesis.util.showToast


class ChoosePictureSourceDialog : DialogFragment(), ChoosePictureSourceDialogView {

    private val choosePictureSourceDialogPresenter = ChoosePictureSourceDialogPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_choose_picture_source, container)
        choosePictureSourceDialogPresenter.attachView(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        val title = arguments?.getString("title", "Choose receipt image source")
        dialog.setTitle(title)

    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        activity?.onBackPressed()
    }

    @OnClick(R.id.receipt_input_button_add_receipt_from_gallery)
    fun onLoadFromGalleryButtonClick() {
        onLoadFromGalleryClick()
    }

    private fun onLoadFromGalleryClick() {
        choosePictureSourceDialogPresenter.onLoadFromGalleryClick(requireActivity())
    }

    @OnClick(R.id.receipt_input_button_add_receipt_from_camera)
    fun onLoadFromCameraButtonClick() {
        onLoadFromCameraClick()
    }

    override fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a picture to load"), FILE_SELECT_REQUEST_CODE)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onLoadFromCameraClick() {
        choosePictureSourceDialogPresenter.onLoadFromCameraClick(requireActivity())
    }

    override fun showCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST_CODE);
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "Please install a camera", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        choosePictureSourceDialogPresenter.handleOnActivityResult(requestCode, resultCode, data, requireActivity())
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun passPicturePath(picturePath: String) {
        targetFragment?.onActivityResult(
                targetRequestCode,
                Activity.RESULT_OK,
                Intent().putExtra(PICTURE_PATH, picturePath)
        )
        dismiss()
    }

    override fun passIntentWithPicture(data: Intent?) {
        targetFragment?.onActivityResult(
                targetRequestCode,
                Activity.RESULT_OK,
                Intent().putExtra(INTENT_WITH_PICTURE_FROM_CAMERA, data)
        )
        dismiss()
    }

    override fun onPermissionDenied() {
        showToast(requireContext(), "The permission is denied permanently - to change it go to options")
        dismiss()
        activity?.onBackPressed()
    }

    companion object {

        const val PICTURE_PATH: String = "PicturePath"

        const val INTENT_WITH_PICTURE_FROM_CAMERA: String = "PictureIntent"

        const val FRAGMENT_TAG: String = "Choose Source Of Image"

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
