package piedel.piotr.thesis.service.drive

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import io.reactivex.Single
import piedel.piotr.thesis.configuration.DEFAULT_GOOGLE_DRIVE_UPLOAD_FOLDER_NAME
import piedel.piotr.thesis.configuration.DEFAULT_GOOGLE_DRIVE_UPLOAD_LOCATION_ROOT
import piedel.piotr.thesis.configuration.TYPE_GOOGLE_DRIVE_FOLDER
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UploadFolderAction(private val googleDriveClient: Drive) {

    fun createAppFolderInGoogleDrive(): Single<GoogleDriveFileMetadataHolder> {
        return createFolderInGoogleDrive(DEFAULT_GOOGLE_DRIVE_UPLOAD_FOLDER_NAME)
    }

    fun createFolderInGoogleDrive(folderName: String): Single<GoogleDriveFileMetadataHolder> {
        return Single.fromCallable {
            val newFolderMetadata: File = createMetadataForFolderInRootDrivePath(folderName)
            val metadataResponseFromGoogleDrive = uploadFolderToGoogleDrive(newFolderMetadata)
            Timber.d(" createFolderInGoogleDrive metadata %s", newFolderMetadata.toString())
            return@fromCallable GoogleDriveFileMetadataHolder(metadataResponseFromGoogleDrive?.id,
                    metadataResponseFromGoogleDrive?.name)
        }
                .timeout(20, TimeUnit.SECONDS)
                .compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())
    }

    private fun createMetadataForFolderInRootDrivePath(folderName: String): File {
        return File()
                .setParents(listOf(DEFAULT_GOOGLE_DRIVE_UPLOAD_LOCATION_ROOT))
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