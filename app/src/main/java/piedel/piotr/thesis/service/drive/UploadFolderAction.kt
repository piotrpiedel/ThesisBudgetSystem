package piedel.piotr.thesis.service.drive

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import io.reactivex.Single
import piedel.piotr.thesis.configuration.DEFAULT_GOOGLE_DRIVE_ACCOUNT_UPLOAD_FOLDER_ROOT
import piedel.piotr.thesis.configuration.DEFAULT_GOOGLE_DRIVE_APP_UPLOAD_FOLDER_NAME
import piedel.piotr.thesis.configuration.TYPE_GOOGLE_DRIVE_FOLDER
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UploadFolderAction(private val googleDriveClient: Drive) {

    fun createAppFolderInRootFolderInGoogleDrive(): Single<GoogleDriveFileMetadataHolder> {
        return createFolderInGoogleDrive(DEFAULT_GOOGLE_DRIVE_APP_UPLOAD_FOLDER_NAME, DEFAULT_GOOGLE_DRIVE_ACCOUNT_UPLOAD_FOLDER_ROOT)
    }

    fun createFolderInGoogleDrive(folderName: String, parentFolderName: String?): Single<GoogleDriveFileMetadataHolder> {
        return Single.fromCallable {
            val newFolderMetadata: File? =
                    if (parentFolderName != null) createMetadataForFolderInDrivePath(folderName, parentFolderName)
                    else createMetadataForFolderInDrivePath(folderName, DEFAULT_GOOGLE_DRIVE_ACCOUNT_UPLOAD_FOLDER_ROOT)

            val metadataResponseFromGoogleDriveForCreatedFolder = uploadFolderToGoogleDrive(newFolderMetadata)
            Timber.d(" createFolderInGoogleDrive metadata %s", newFolderMetadata.toString())
            return@fromCallable GoogleDriveFileMetadataHolder(metadataResponseFromGoogleDriveForCreatedFolder?.id,
                    metadataResponseFromGoogleDriveForCreatedFolder?.name)
        }
                .timeout(20, TimeUnit.SECONDS)
                .compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())
    }

    private fun createMetadataForFolderInDrivePath(folderName: String, parentFolder: String): File? {
        return createFolderInGoogleDriveSpace(folderName, parentFolder)
    }

    private fun createFolderInGoogleDriveSpace(folderName: String, parentFolder: String): File? {
        return File()
                .setParents(listOf(parentFolder))
                .setMimeType(TYPE_GOOGLE_DRIVE_FOLDER)
                .setName(folderName)
    }

    private fun uploadFolderToGoogleDrive(fileToUploadMetadata: File?): File? {
        return googleDriveClient
                .files()
                .create(fileToUploadMetadata)
                .execute()
    }
}