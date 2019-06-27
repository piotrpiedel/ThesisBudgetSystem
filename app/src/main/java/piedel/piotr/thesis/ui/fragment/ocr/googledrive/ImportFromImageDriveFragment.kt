package piedel.piotr.thesis.ui.fragment.ocr.googledrive

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.DriveScopes
import piedel.piotr.thesis.R
import piedel.piotr.thesis.configuration.REQUEST_CODE_SIGN_IN
import piedel.piotr.thesis.configuration.START_IMAGE_PICKER_ACTIVITY_REQUEST_CODE
import piedel.piotr.thesis.data.buildier.ImagePickerActivityOptions
import piedel.piotr.thesis.ui.activity.imagepicker.ImagePickerActivity
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.showToastLong
import timber.log.Timber
import javax.inject.Inject


class ImportFromImageDriveFragment : BaseFragment(), ImportFromImageDriveContract.ImportFromImageDriveView {

    @BindView(R.id.import_file_localization_drive)
    lateinit var importedTextFromOcr: TextView

    @BindView(R.id.import_picture_drive)
    lateinit var receiptPictureOCRLoaded: ImageView

    override val layout: Int
        get() = R.layout.fragment_import_from_image_drive_ocr
    override val toolbarTitle: String
        get() = context?.getString(R.string.import_from_image_drive).toString()

    @Inject
    lateinit var importFromImageDrivePresenter: ImportFromImageDrivePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        importFromImageDrivePresenter.attachView(this)
    }

    @OnClick(R.id.fragment_import_drive_button_load)
    fun onLoadButtonClicked() {
        checkIfSignInWithAccount()
    }

    override fun checkPermissionsAndOpenFilePicker() {
        importFromImageDrivePresenter.checkPermissions(requireActivity())
    }

    private fun checkIfSignInWithAccount() {
        importFromImageDrivePresenter.signWithAccount()
    }

    override fun requestSignIn(signInOptions: GoogleSignInOptions) {
        val signInClient = GoogleSignIn.getClient(requireActivity(), signInOptions)
        startActivityForResult(signInClient.signInIntent, REQUEST_CODE_SIGN_IN);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        importFromImageDrivePresenter.handleOnActivityResult(requestCode, resultCode, data)
    }

    override fun getGoogleAccountCredentialUsingOAuth2(): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(requireContext(), listOf(DriveScopes.DRIVE_FILE))
    }

    override fun getAlreadySignedAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(requireContext())
    }

    override fun setImageViewWithBitmap(resource: Bitmap?) {
        receiptPictureOCRLoaded.setImageBitmap(resource)
    }

    override fun showError() {
        Toast.makeText(context, getString(R.string.something_went_wrong_try_again_later), Toast.LENGTH_SHORT).show()
    }

    override fun errorNetworkConnection() {
        showToastLong(requireContext(), "Unable to resolve host - check your network connection")
    }

    override fun showImageContainsNoText() {
        showToastLong(requireContext(), "Passed image did not contain any text - please input proper image")
    }

    override fun errorParsingReceipt() {
        showToastLong(requireContext(), "Can't proceed OCR for receipt - " +
                "not recognized format of receipt input or not containing operations - please send us report with image of receipt")
    }

    override fun showInsertCompleteToast() {
        Toast.makeText(context, getString(R.string.loading_operation_from_html_success), Toast.LENGTH_SHORT).show()
    }

    override fun startImagePickerActivity() {
        launchImagePickerActivity()
    }

    private fun launchImagePickerActivity() {
        val intent = Intent(requireContext(), ImagePickerActivity::class.java)
                .putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTIONS_PARCELABLE, ImagePickerActivityOptions())
        startActivityForResult(intent, START_IMAGE_PICKER_ACTIVITY_REQUEST_CODE);
    }

    companion object {
        const val FRAGMENT_TAG: String = "importFromImageDriveFragment"
    }
}