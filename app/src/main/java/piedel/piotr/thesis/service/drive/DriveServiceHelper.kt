package piedel.piotr.thesis.service.drive

import android.annotation.SuppressLint
import com.google.api.services.drive.Drive
import io.reactivex.Single
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import timber.log.Timber


/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
class DriveServiceHelper(private val googleDriveClient: Drive) {

    @SuppressLint("CheckResult")
    fun uploadImageFileAsGoogleDocsToAppRootFolder(pathFile: String?): Single<GoogleDriveFileMetadataHolder> {
        return getAppFolderFromGoogleDrive()
                .flatMap { folder ->
                    Timber.d("flatMapSingle when folder was found - folder name: %s, folderID: (%s)", folder.name, folder.id)
                    return@flatMap uploadImageFileAsGoogleDocsToAppRootFolder(pathFile, folder)
                }
                .doOnError {
                    Timber.d("uploadImageFileAsGoogleDocsToAppRootFolder error message is: %s ", it.localizedMessage)
                }
                .onErrorResumeNext(retryUploadFilActionWithCreatingNewAppFolder(pathFile))
    }

    private fun retryUploadFilActionWithCreatingNewAppFolder(pathFile: String?): Single<GoogleDriveFileMetadataHolder> {
        return UploadFolderAction(googleDriveClient)
                .createAppFolderInRootFolderInGoogleDrive()
                .flatMap { folder ->
                    Timber.d("flatMapSingle when folder was created (not found for first time) : folder name: %s, folderID: (%s)"
                            , folder.name, folder.id)

                    return@flatMap uploadImageFileAsGoogleDocsToAppRootFolder(pathFile, folder)
                }
    }

    private fun uploadImageFileAsGoogleDocsToAppRootFolder(pathFile: String?, folder: GoogleDriveFileMetadataHolder) =
            UploadFileAction(googleDriveClient)
                    .uploadImageFileAsGoogleDocsToAppRootFolder(pathFile, folder.id)

    fun downloadConvertedFileToString(fileId: String): Single<GoogleDriveResponseHolder> {
        return DownloadFileAction(googleDriveClient).downloadConvertedFileToString(fileId)
    }

    private fun getAppFolderFromGoogleDrive(): Single<GoogleDriveFileMetadataHolder> {
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