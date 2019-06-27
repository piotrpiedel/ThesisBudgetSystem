package piedel.piotr.thesis.ui.activity.imagepicker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.yalantis.ucrop.UCrop
import droidninja.filepicker.FilePickerConst
import piedel.piotr.thesis.R
import piedel.piotr.thesis.configuration.FILE_PICKER_BUILDER_IMAGE_REQUEST_CODE
import piedel.piotr.thesis.data.buildier.ImagePickerActivityOptions
import piedel.piotr.thesis.ui.activity.imagepicker.ImagePickerContract.ImagePickerView
import piedel.piotr.thesis.ui.base.BaseActivity
import piedel.piotr.thesis.util.getImageFilePicker
import piedel.piotr.thesis.util.showToast
import piedel.piotr.thesis.util.suffixAppendToFileNameBeforeExtension
import timber.log.Timber
import java.io.File
import javax.inject.Inject


class ImagePickerActivity : BaseActivity(), ImagePickerView {

    override val layout: Int
        get() = R.layout.activity_image_picker

    @Inject
    lateinit var imagePickerPresenter: ImagePickerPresenter

    private var imagePickerOptions: ImagePickerActivityOptions = ImagePickerActivityOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        imagePickerPresenter.attachView(this)
        setContentView(R.layout.activity_image_picker)
        getImagePickerOptionsFromIntentOrDefault()
        pickImageFile()
    }

    private fun getImagePickerOptionsFromIntentOrDefault() {
        if (isImagePickerOptionsAttached())
            imagePickerOptions = intent.getParcelableExtra(INTENT_IMAGE_PICKER_OPTIONS_PARCELABLE)
    }

    private fun isImagePickerOptionsAttached(): Boolean = intent.hasExtra(INTENT_IMAGE_PICKER_OPTIONS_PARCELABLE)

    private fun pickImageFile() {
        imagePickerPresenter.openPickerBasedOnGivenPermissions(this)
    }

    override fun showFileChooserOnlyGallery() {
        getImageFilePicker(this, false, FILE_PICKER_BUILDER_IMAGE_REQUEST_CODE)
    }

    override fun showFileChooserGalleryAndCamera() {
        getImageFilePicker(this, true, FILE_PICKER_BUILDER_IMAGE_REQUEST_CODE)
    }

    override fun onPermissionPermanentlyDenied() {
        showToast(this, getString(R.string.the_permission_is_denied_permanently))
    }

    override fun showToastWithRequestOfPermissions() {
        showToast(this, getString(R.string.permission_required_storage_camera_optional))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_PICKER_BUILDER_IMAGE_REQUEST_CODE ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val stringPath = data.extras?.getStringArrayList(FilePickerConst.KEY_SELECTED_MEDIA)?.first() // using KEY_SELECTED_MEDIA return Array<String>
                    cropImage(stringPath)
                } else {
                    setResultCancelled()
                }
            UCrop.REQUEST_CROP -> if (resultCode == Activity.RESULT_OK) {
                handleUCropResult(data)
            } else {
                setResultCancelled()
            }
            UCrop.RESULT_ERROR -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val cropError = UCrop.getError(data)
                    Timber.d(cropError, "Crop error ")
                    setResultCancelled()
                }
            }
            else -> setResultCancelled()
        }
    }

    private fun cropImage(sourcePathToFile: String?) {
        val uriFromString = Uri.fromFile(File(sourcePathToFile))
        val destinationUri = Uri.parse(uriFromString.toString().suffixAppendToFileNameBeforeExtension("_crop"))
        val options = UCrop.Options().apply {
            setCompressionQuality(imagePickerOptions.imageCompression)
            setTheme(R.style.AppTheme)
        }
        if (imagePickerOptions.lockAspectRatio)
            options.withAspectRatio(imagePickerOptions.aspectRatio_X, imagePickerOptions.aspectRatio_Y)

        if (imagePickerOptions.setBitmapMaxWidthHeight)
            options.withMaxResultSize(imagePickerOptions.bitmapMaxWidth, imagePickerOptions.bitmapMaxHeight)

        UCrop.of(uriFromString, destinationUri)
                .withOptions(options)
                .start(this)
    }


    private fun handleUCropResult(data: Intent?) {
        if (data == null) {
            setResultCancelled()
            return
        }
        val resultUri = UCrop.getOutput(data)?.path
        setResultOk(resultUri)
    }

    private fun setResultOk(imagePath: String?) {
        val intent = Intent()
        intent.putExtra(INTENT_RESULT_PATH_IMAGE_PICKER_ACTIVITY, imagePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setResultCancelled() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    companion object {
        const val INTENT_IMAGE_PICKER_OPTIONS_PARCELABLE = "image_picker_options_parcelable"
        const val INTENT_RESULT_PATH_IMAGE_PICKER_ACTIVITY = "path_result_from_image_activity"
    }
}