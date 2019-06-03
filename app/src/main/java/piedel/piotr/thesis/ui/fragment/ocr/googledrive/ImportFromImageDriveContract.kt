package piedel.piotr.thesis.ui.fragment.ocr.googledrive

import android.content.Intent
import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface ImportFromImageDriveContract {
    interface ImportFromImageDriveView : BaseView {
        fun showError()

        fun showFileChooserOnlyGallery()

        fun showFileChooserGalleryAndCamera()

        fun onPermissionPermanentlyDenied()

        fun onPermissionPartiallyDenied()

        fun showToastWithRequestOfPermissions()

        fun setImageViewWithBitmap(resource: Bitmap?)

        fun requestSignIn(signInOptions: GoogleSignInOptions)

        fun checkPermissionsAndOpenFilePicker()

        fun getGoogleAccountCredentialUsingOAuth2(): GoogleAccountCredential

        fun getAlreadySignedAccount(): GoogleSignInAccount?
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun checkPermissions(fragmentActivity: FragmentActivity)

        fun handlePickingFileResult(resultCode: Int, data: Intent)

        fun signWithAccount()

        fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}