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
import io.reactivex.CompletableObserver
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
import java.net.UnknownHostException
import javax.inject.Inject


@ConfigPersistent
class ImportFromImageDrivePresenter @Inject constructor(private val operationsRepository: OperationRepository) : BasePresenter<ImportFromImageDriveView>(), PresenterContract<ImportFromImageDriveView> {

    private var mDriveServiceHelper: DriveServiceHelper? = null
    private var disposable: Disposable? = null


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

        disposable = mDriveServiceHelper?.uploadImageFileToRootFolder(stringPath)
                ?.flatMap { fileLocatedOnGoogleDrive ->
                    mDriveServiceHelper?.downloadConvertedFileToString(fileLocatedOnGoogleDrive.id.toString())
                }
                ?.subscribe(
                        { googleDriveResponseHolder ->
                            if (googleDriveResponseHolder.plainTextFromOutputStream.isEmpty()
                                    || googleDriveResponseHolder.plainTextFromOutputStream.isBlank()) {
                                view?.showImageContainsNoText()
                                return@subscribe
                            }
                            //Fix if not operations contain itd; need to block some of operations on my app
                            val googleDriveResponseParser = GoogleDriveResponseParser(googleDriveResponseHolder)
                            if (!googleDriveResponseParser.listOfOperations.isNullOrEmpty())
                                insertOperation(*googleDriveResponseParser.listOfOperations.toTypedArray())
                            else view?.errorParsingReceipt()
                        },
                        { e ->
                            if (e is UnknownHostException) {
                                view?.errorNetworkConnection()
                            }
                            Timber.d("onError:  %s", e.toString())
                        }
                )
        addDisposable(disposable)
    }

    private fun insertOperation(vararg operation: Operation) {
        operationsRepository.insertOperation(*operation)
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Timber.d("onComplete insertOperation ")
                        view?.showInsertCompleteToast()
                    }

                    override fun onSubscribe(d: Disposable) {
                        Timber.d("onComplete insertOperation ")
                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e, "onError insertOperation")
                    }
                })
    }

    override fun signWithAccount() {
        val signedAccount: GoogleSignInAccount? = view?.getAlreadySignedAccount()
        signedAccount?.let {
            createDriveServiceHelper(signedAccount) // with already signed account
        } ?: run {
            view?.requestSignIn(getSignInOptions()) // request sign in and handle result
            // in handleSignInRequestResult(result: Intent) function
        }
    }

    private fun getSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()
    }

    private fun handleSignInRequestResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener { googleAccount ->
                    createDriveServiceHelper(googleAccount)
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
        mDriveServiceHelper = DriveServiceHelper.getInstance(googleDriveService) // TODO: refactor this somehow
        importFile()
    }

    private fun importFile() {
        view?.checkPermissionsAndOpenFilePicker()
    }
}