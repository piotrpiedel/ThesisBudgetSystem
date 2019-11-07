package piedel.piotr.thesis.service.drive

import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import io.reactivex.Observable
import io.reactivex.Single
import piedel.piotr.thesis.configuration.DEFAULT_GOOGLE_DRIVE_ACCOUNT_UPLOAD_FOLDER_ROOT
import piedel.piotr.thesis.configuration.TYPE_GOOGLE_DOCS
import piedel.piotr.thesis.configuration.TYPE_PHOTO
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UploadFileAction(private val googleDriveClient: Drive) {

    fun uploadFile(localFile: java.io.File, mimeType: String, folderId: String?): Observable<GoogleDriveFileMetadataHolder> {
        return Observable.fromCallable {
            val metadata = File()
                    .setParents(getFolderListOrGetDefaultRootDriveFolder(folderId))
                    .setMimeType(mimeType)
                    .setName(localFile.name)

            val fileContent = FileContent(mimeType, localFile)

            val fileMeta = googleDriveClient.files().create(metadata, fileContent).execute()
            return@fromCallable GoogleDriveFileMetadataHolder(fileMeta.id, fileMeta.name)
        }
                .timeout(20, TimeUnit.SECONDS)
                .compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())
    }

    fun uploadImageFileAsGoogleDocsToAppRootFolder(pathFile: String?, folderId: String?): Single<GoogleDriveFileMetadataHolder> {
        return uploadImageFileAsGoogleDocs(pathFile, folderId)
    }

    private fun uploadImageFileAsGoogleDocs(pathFile: String?, folderId: String?): Single<GoogleDriveFileMetadataHolder> {
        return Single.fromCallable {
            val fileFromPath: java.io.File? = createFileFromPathOrReturnNull(pathFile)

            val metadataForGoogleGoogleDocs = getMetadataForGoogleDocsFile(
                    getFolderListOrGetDefaultRootDriveFolder(folderId), fileFromPath) // it decide how document will be called;

            val metadataResponseFromGoogleDriveForCreatedFile = uploadFileToGoogleDrive(metadataForGoogleGoogleDocs, fileFromPath)
            Timber.d("Uploaded file from image with id: %s, name: %s, folderId: %s", metadataResponseFromGoogleDriveForCreatedFile?.id,
                    metadataResponseFromGoogleDriveForCreatedFile?.name, folderId)
            return@fromCallable GoogleDriveFileMetadataHolder(metadataResponseFromGoogleDriveForCreatedFile?.id,
                    metadataResponseFromGoogleDriveForCreatedFile?.name)
        }
                .timeout(20, TimeUnit.SECONDS)
                .compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())
    }

    private fun uploadFileToGoogleDrive(metadata: File?, createdFileFromPath: java.io.File?): File? {
        val fileContentForImageFile = FileContent(TYPE_PHOTO, createdFileFromPath)
        return googleDriveClient.files()
                .create(metadata, fileContentForImageFile)
                .execute()
    }

    private fun createFileFromPathOrReturnNull(pathToFile: String?): java.io.File? {
        var fileFromPath: java.io.File? = null
        if (pathToFile != null) {
            fileFromPath = java.io.File(pathToFile)
        }
        return fileFromPath
    }

    private fun getMetadataForGoogleDocsFile(pathFoldersForGoogleDriveToPutFile: List<String>, createdFileFromPath: java.io.File?): File? {
        return File()
                .setParents(pathFoldersForGoogleDriveToPutFile)
                .setMimeType(TYPE_GOOGLE_DOCS) // uploading image to google docs;
                .setName(createdFileFromPath?.name)
    }

    private fun getFolderListOrGetDefaultRootDriveFolder(folderId: String?): List<String> {
        return if (folderId == null) {
            listOf(DEFAULT_GOOGLE_DRIVE_ACCOUNT_UPLOAD_FOLDER_ROOT)
        } else {
            listOf(folderId)
        }
    }

}