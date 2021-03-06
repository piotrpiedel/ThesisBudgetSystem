package piedel.piotr.thesis.ui.fragment.ocr.googledrive

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface ImportFromImageDriveContract {
    interface ImportFromImageDriveView : BaseView {
        fun showError()

        fun requestSignIn(signInOptions: GoogleSignInOptions)

        fun checkPermissionsAndOpenFilePicker()

        fun getGoogleAccountCredentialUsingOAuth2(): GoogleAccountCredential

        fun getAccountIfAlreadySigned(): GoogleSignInAccount?

        fun errorNetworkConnection()

        fun showImageContainsNoText()

        fun errorParsingReceipt()

        fun startImagePickerActivity()

        fun setImportedTextFromImage(plainTextFromOutputStream: String)

        fun setDividedStringOnlyForDebuggingPurposes(dividedStringPublicForDebugging: List<String>)

        fun passListToOperationSelectionFragment(listOfOperations: List<Operation>)
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun checkPermissions(fragmentActivity: FragmentActivity)

        fun signWithAccountAndLoadImage()

        fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}