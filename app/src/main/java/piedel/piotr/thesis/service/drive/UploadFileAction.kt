package piedel.piotr.thesis.service.drive

import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import io.reactivex.Observable
import io.reactivex.Single
import piedel.piotr.thesis.configuration.DEFAULT_GOOGLE_DRIVE__ACCOUNT_UPLOAD_FOLDER_ROOT
import piedel.piotr.thesis.configuration.TYPE_GOOGLE_DOCS
import piedel.piotr.thesis.configuration.TYPE_PHOTO
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import java.util.concurrent.TimeUnit

class UploadFileAction(private val googleDriveClient: Drive) {


    fun uploadFile(localFile: java.io.File, mimeType: String, folderId: String?): Observable<GoogleDriveFileMetadataHolder> {
        return Observable.fromCallable {
            val metadata = File()
                    .setParents(getFolderList(folderId))
                    .setMimeType(mimeType)
                    .setName(localFile.name)

            val fileContent = FileContent(mimeType, localFile)

            val fileMeta = googleDriveClient.files().create(metadata, fileContent).execute()
            return@fromCallable GoogleDriveFileMetadataHolder(fileMeta.id, fileMeta.name)
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())

    }

    fun uploadImageFileToAppRootFolder(pathFile: String?, folderId: String?): Single<GoogleDriveFileMetadataHolder> {
        return uploadImageFile(pathFile, folderId)
    }

    fun uploadImageFileToRootFolder(pathFile: String?): Single<GoogleDriveFileMetadataHolder> {
        return uploadImageFile(pathFile, null)
    }

    private fun uploadImageFile(pathFile: String?, folderId: String?): Single<GoogleDriveFileMetadataHolder> {
        return Single.fromCallable {
            val createdFileFromPath: java.io.File? = createFileFromPathOrReturnNull(pathFile)
            val metadata = getMetadataForImageFile(getFolderList(folderId), createdFileFromPath)  // it decide how document will be called;
            val fileMeta = getFileMeta(metadata, createdFileFromPath)
            return@fromCallable GoogleDriveFileMetadataHolder(fileMeta?.id, fileMeta?.name)
        }
                .timeout(20, TimeUnit.SECONDS)
                .compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())
    }

    private fun getFileMeta(metadata: File?, createdFileFromPath: java.io.File?): File? {
        return googleDriveClient.files()
                .create(metadata, FileContent(TYPE_PHOTO, createdFileFromPath))
                .execute()
    }

    private fun createFileFromPathOrReturnNull(pathFile: String?): java.io.File? {
        var createdFileFromPath: java.io.File? = null
        pathFile?.let {
            createdFileFromPath = java.io.File(pathFile)
        }
        return createdFileFromPath
    }

    private fun getMetadataForImageFile(root: List<String>, createdFileFromPath: java.io.File?): File? {
        return File()
                .setParents(root)
                .setMimeType(TYPE_GOOGLE_DOCS) // uploading image to google docs;
                .setName(createdFileFromPath?.name)
    }

    private fun getFolderList(folderId: String?): List<String> {
        return if (folderId == null) {
            listOf(DEFAULT_GOOGLE_DRIVE__ACCOUNT_UPLOAD_FOLDER_ROOT)
        } else {
            listOf(folderId)
        }
    }

}