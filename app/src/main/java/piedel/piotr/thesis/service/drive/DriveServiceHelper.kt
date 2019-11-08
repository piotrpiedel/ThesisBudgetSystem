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
                    Timber.d("flatMapSingle when folder was found: %s (%s)", folder.name, folder.id)
                    return@flatMap UploadFileAction(googleDriveClient)
                            .uploadImageFileAsGoogleDocsToAppRootFolder(pathFile, folder.id)
                }
                .doOnError {
                    Timber.d("uploadImageFileAsGoogleDocsToAppRootFolder error message: %s ", it.localizedMessage)
                }
                .onErrorResumeNext(
                        UploadFolderAction(googleDriveClient)
                                .createAppFolderInRootFolderInGoogleDrive()
                                .flatMap { folder ->
                                    Timber.d("flatMapSingle when folder was created (not found for first time) : %s (%s)"
                                            , folder.name, folder.id)

                                    return@flatMap UploadFileAction(googleDriveClient)
                                            .uploadImageFileAsGoogleDocsToAppRootFolder(pathFile, folder.id)
                                })
    }

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