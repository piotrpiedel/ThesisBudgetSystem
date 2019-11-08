package piedel.piotr.thesis.ui.fragment.ocr.googledrive

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.configuration.REQUEST_CODE_SIGN_IN
import piedel.piotr.thesis.configuration.START_IMAGE_PICKER_ACTIVITY_REQUEST_CODE
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.service.drive.DriveServiceHelper
import piedel.piotr.thesis.ui.activity.imagepicker.ImagePickerActivity.Companion.INTENT_RESULT_PATH_IMAGE_PICKER_ACTIVITY
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.ocr.googledrive.ImportFromImageDriveContract.ImportFromImageDriveView
import piedel.piotr.thesis.ui.fragment.ocr.googledrive.ImportFromImageDriveContract.PresenterContract
import piedel.piotr.thesis.util.gdrive.GoogleDriveResponseParser
import timber.log.Timber
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject


@ConfigPersistent
class ImportFromImageDrivePresenter @Inject constructor(private val operationsRepository: OperationRepository) : BasePresenter<ImportFromImageDriveView>(), PresenterContract<ImportFromImageDriveView> {

    private var driveServiceHelper: DriveServiceHelper? = null
    private var disposable: Disposable? = null
    private var signedAccountInstance: GoogleSignInAccount? = null

    override fun checkPermissions(fragmentActivity: FragmentActivity) {
        showImagePickerOptions()
    }

    override fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            START_IMAGE_PICKER_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    handlePickingFileResult(data)
                }
            }
            REQUEST_CODE_SIGN_IN -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Timber.d("handleOnActivityResult REQUEST_CODE_SIGN_IN is inside if")
                    handleSignInRequestResult(data)
                }
            }
        }
    }

    private fun showImagePickerOptions() {
        view?.startImagePickerActivity()
    }


    private fun handlePickingFileResult(data: Intent) {
        // TODO: check if path is correct
        val stringPath = data.getStringExtra(INTENT_RESULT_PATH_IMAGE_PICKER_ACTIVITY)
        createObservableOfOCRResult(stringPath)
    }

    private fun createObservableOfOCRResult(stringPath: String?) {
        disposable = driveServiceHelper?.uploadImageFileAsGoogleDocsToAppRootFolder(stringPath)
                ?.flatMap { fileLocatedOnGoogleDrive ->
                    driveServiceHelper?.downloadConvertedFileToString(fileLocatedOnGoogleDrive.id.toString())
                }
                ?.subscribe(
                        { googleDriveResponseHolder ->
                            if (googleDriveResponseHolder.plainTextFromOutputStream.isEmpty()
                                    || googleDriveResponseHolder.plainTextFromOutputStream.isBlank()) {
                                view?.showImageContainsNoText()
                                return@subscribe
                            }
                            Timber.d("Response of text OCRed from image by GoogleDrive: %s",
                                    googleDriveResponseHolder.plainTextFromOutputStream)

                            //Fix if not operations contain itd; need to block some of operations on my app
                            val googleDriveResponseParser = GoogleDriveResponseParser(googleDriveResponseHolder)
                            val listOfParsedOperationsFromOCRString = googleDriveResponseParser
                                    .parseStringFromOcrToListOfOperations()

                            setTextViewWithUnParsedTextFromOCR(googleDriveResponseParser)  // TODO: delete this before official relaease

                            if (isGoogleDriveResponseParserContainingOperations(listOfParsedOperationsFromOCRString)) {
                                view?.passListToOperationSelectionFragment(listOfParsedOperationsFromOCRString)
                            } else showErrorDuringParsingReceipt()
                        },
                        { error ->
                            if (error is UnknownHostException || error is SocketException) {
                                view?.errorNetworkConnection()
                            }
                            Timber.d("onError:  %s", error.toString())
                        }
                )
        addDisposable(disposable)
    }

    private fun isGoogleDriveResponseParserContainingOperations(listOfParsedOperationsFromOCRString: MutableList<Operation>) =
            !listOfParsedOperationsFromOCRString
                    .isNullOrEmpty()

    private fun setTextViewWithUnParsedTextFromOCR(googleDriveResponseParser: GoogleDriveResponseParser) {
        Timber.d("setDividedStringOnlyForDebuggingPurpose: %s", googleDriveResponseParser.dividedStringPublicForDebugging)
    }

    private fun showErrorDuringParsingReceipt() {
        view?.errorParsingReceipt()
    }

    override fun signWithAccountAndLoadImage() {
        signedAccountInstance = view?.getAccountIfAlreadySigned()
        if (signedAccountInstance == null) {
            // request sign in and handle result
            // in handleSignInRequestResult(result: Intent) function
            view?.requestSignIn(getGoogleDriveSignInOptions())
        } else {
            createDriveServiceHelper(signedAccountInstance)
        }
    }

    private fun getGoogleDriveSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()
    }

    private fun handleSignInRequestResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener { googleAccount ->
                    signedAccountInstance = googleAccount
                    createDriveServiceHelper(signedAccountInstance)
                }
                .addOnFailureListener { exception ->
                    Timber.e(exception, "Unable to sign in ")
                }
    }

    private fun createDriveServiceHelper(account: GoogleSignInAccount?) {
        val credential = view?.getGoogleAccountCredentialUsingOAuth2()?.setSelectedAccount(account?.account)
        createDriveHelper(createGoogleDriveService(credential))
    }

    private fun createGoogleDriveService(credential: GoogleAccountCredential?): Drive {
        return Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                GsonFactory(),
                credential)
                .build()
    }

    private fun createDriveHelper(googleDriveService: Drive) {
        Timber.d("fun createDriveHelper")
        driveServiceHelper = DriveServiceHelper.getInstance(googleDriveService) // TODO: refactor this somehow
        importFile()
    }

    private fun importFile() {
        view?.checkPermissionsAndOpenFilePicker()
    }
}