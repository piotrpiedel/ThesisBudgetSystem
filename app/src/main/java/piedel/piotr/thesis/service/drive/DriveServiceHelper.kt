package piedel.piotr.thesis.service.drive

import android.annotation.SuppressLint
import com.google.api.services.drive.Drive
import io.reactivex.Maybe
import io.reactivex.Single
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import timber.log.Timber


/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
class DriveServiceHelper(private val googleDriveClient: Drive) {

    @SuppressLint("CheckResult")
    fun uploadImageFileAsGoogleDocsToAppRootFolder(pathFile: String?): Single<GoogleDriveFileMetadataHolder> {
        return getAppFolderFromGoogleDrive()
                .flatMapSingle { folder ->
                    UploadFileAction(googleDriveClient).uploadImageFileAsGoogleDocsToAppRootFolder(pathFile, folder.id)
                }
                .doOnError { Timber.d("uploadImageFileAsGoogleDocsToAppRootFolder error message: %s ", it.localizedMessage) }
                .onErrorResumeNext(UploadFolderAction(googleDriveClient).createAppFolderInRootFolderInGoogleDrive()
                        .flatMap { folder ->
                            UploadFileAction(googleDriveClient).uploadImageFileAsGoogleDocsToAppRootFolder(pathFile, folder.id)
                        })
    }

    private fun getAppFolderFromGoogleDrive(): Maybe<GoogleDriveFileMetadataHolder> {
        val searchForFolderAction = SearchForFolderAction(googleDriveClient)
        return searchForFolderAction.searchForAppRootFolder()
    }


    companion object {
        @Volatile
        private var INSTANCE: DriveServiceHelper? = null

        fun getInstance(mDriveService: Drive): DriveServiceHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: DriveServiceHelper(mDriveService)
                }
    }
}