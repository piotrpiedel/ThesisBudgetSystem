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
import piedel.piotr.thesis.configuration.FILE_PICKER_BUILDER_IMAGE_REQUEST_CODE
import piedel.piotr.thesis.configuration.REQUEST_CODE_SIGN_IN
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.getImageFilePicker
import piedel.piotr.thesis.util.showToast
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

    override fun showFileChooserOnlyGallery() {
        getImageFilePicker(this, false, FILE_PICKER_BUILDER_IMAGE_REQUEST_CODE)
    }

    override fun showFileChooserGalleryAndCamera() {
        getImageFilePicker(this, true, FILE_PICKER_BUILDER_IMAGE_REQUEST_CODE)
    }

    override fun onPermissionPermanentlyDenied() {
        showToast(requireContext(), getString(R.string.the_permission_is_denied_permanently))
    }

    override fun showToastWithRequestOfPermissions() {
        showToast(requireContext(), getString(R.string.permission_required_storage_camera_optional))
    }

    override fun requestSignIn(signInOptions: GoogleSignInOptions) {
        Timber.d("Request Sign in fun requestSignIn")
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

    companion object {
        const val FRAGMENT_TAG: String = "importFromImageDriveFragment"
    }
}