package piedel.piotr.thesis.service.drive

import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import io.reactivex.Observable
import piedel.piotr.thesis.configuration.TYPE_GOOGLE_DOCS
import piedel.piotr.thesis.configuration.TYPE_PHOTO
import piedel.piotr.thesis.configuration.TYPE_TEXT_PLAIN
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileHolder
import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
class DriveServiceHelper(private val mDriveService: Drive) {

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */

    fun createTextFile(fileName: String, content: String, folderId: String?): Observable<GoogleDriveFileHolder> {
        return Observable.fromCallable {

            val root: List<String> = if (folderId == null) {
                Collections.singletonList("root")
            } else {
                Collections.singletonList(folderId)
            }
            val metadata = File()
                    .setParents(root)
                    .setMimeType("text/plain")
                    .setName("TEST 1 Untitled file")
            val contentStream = ByteArrayContent.fromString("text/plain", content)

            val googleFile = mDriveService.files().create(metadata, contentStream).execute()
                    ?: throw IOException("Null result when requesting file creation.")
            val googleDriveFileHolder = GoogleDriveFileHolder()
            googleDriveFileHolder.id = googleFile.id
            return@fromCallable googleDriveFileHolder
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileHolder>())
    }


    fun createAndUploadImageFile(fileName: String, content: String, folderId: String?): Observable<GoogleDriveFileHolder> {
        return Observable.fromCallable {

            val root: List<String> = if (folderId == null) {
                Collections.singletonList("root")
            } else {
                Collections.singletonList(folderId)
            }
            val metadata = File()
                    .setParents(root)
                    .setMimeType("text/plain")
                    .setName("TEST 1 Untitled file")
            val contentStream = ByteArrayContent.fromString("text/plain", content)

            val googleFile = mDriveService.files().create(metadata, contentStream).execute()
                    ?: throw IOException("Null result when requesting file creation.")
            val googleDriveFileHolder = GoogleDriveFileHolder()
            googleDriveFileHolder.id = googleFile.id
            return@fromCallable googleDriveFileHolder
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileHolder>())
    }

    /**
     * Opens the file identified by `fileId` and returns a [Pair] of its name and
     * contents.
     */
    fun readFile(fileId: String): Observable<Pair<String, String>> {
        return Observable.fromCallable {
            // Retrieve the metadata as a File object.
            val metadata = mDriveService.files().get(fileId).execute()
            val name = metadata.name

            // Stream the file contents to a String.
            mDriveService.files().get(fileId).executeMediaAsInputStream().use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val stringBuilder = StringBuilder()
                    var line: String
                    while (reader.readLine() != null) {
                        line = reader.readLine()
                        stringBuilder.append(line)
                    }
                    val contents = stringBuilder.toString()
                    return@fromCallable Pair(name, contents)
                }
            }
        }
                .compose(SchedulerUtils.ioToMain<Pair<String, String>>())
    }

    /**
     * Updates the file identified by `fileId` with the given `name` and `content`.
     */
    fun saveFile(fileId: String, name: String, content: String): Observable<Unit> {
        return Observable.fromCallable {
            // Create a File containing any metadata changes.
            val metadata = File().setName(name)
            // Convert content to an AbstractInputStreamContent instance.
            val contentStream = ByteArrayContent.fromString("text/plain", content)
            // Update the metadata and contents.
            mDriveService.files().update(fileId, metadata, contentStream).execute()
            return@fromCallable Unit
        }.compose(SchedulerUtils.ioToMain<Unit>())
    }

    /**
     * Returns a [FileList] containing all the visible files in the user's My Drive.
     *
     *
     * The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the [Google
 * Developer's Console](https://play.google.com/apps/publish) and be submitted to Google for verification.
     */

    fun queryFiles(): Observable<FileList> {
        return Observable.fromCallable {
            return@fromCallable mDriveService.files().list().setSpaces("drive").execute()
        }.compose(SchedulerUtils.ioToMain<FileList>())
    }


    fun uploadFile(localFile: java.io.File, mimeType: String, folderId: String?): Observable<GoogleDriveFileHolder> {
        return Observable.fromCallable {
            val metadata = File()
                    .setParents(getFolderList(folderId))
                    .setMimeType(mimeType)
                    .setName(localFile.name)

            val fileContent = FileContent(mimeType, localFile)

            val fileMeta = mDriveService.files().create(metadata, fileContent).execute()
            return@fromCallable GoogleDriveFileHolder(fileMeta.id, fileMeta.name)
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileHolder>())

    }

    fun uploadImageFileToRootFolder(pathFile: String?): Observable<GoogleDriveFileHolder> { // TODO: upload to custom folder
        return uploadImageFile(pathFile, null)
    }

    private fun uploadImageFile(pathFile: String?, folderId: String?): Observable<GoogleDriveFileHolder> {
        return Observable.fromCallable {
            val createdFileFromPath: java.io.File? = createFileFromPathOrGetDefault(pathFile)
            val metadata = getMetadataForImageFile(getFolderList(folderId), createdFileFromPath)  // it decide how document will be called;
            val fileMeta = getFileMeta(metadata, createdFileFromPath)
            return@fromCallable GoogleDriveFileHolder(fileMeta?.id, fileMeta?.name)
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileHolder>())

    }

    private fun getFileMeta(metadata: File?, createdFileFromPath: java.io.File?): File? {
        return mDriveService.files()
                .create(metadata, FileContent(TYPE_PHOTO, createdFileFromPath))
                .execute()
    }

    private fun createFileFromPathOrGetDefault(pathFile: String?): java.io.File? {
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
            listOf("root")
        } else {
            listOf(folderId)
        }
    }

    fun searchFile(fileName: String, mimeType: String): Observable<GoogleDriveFileHolder> {
        return Observable.fromCallable {
            val result = mDriveService.files().list()
                    .setQ("name = '$fileName' and mimeType ='$mimeType'")
                    .setSpaces("drive")
                    .setFields("files(id, name,size,createdTime,modifiedTime,starred)")
                    .execute()
            if (result.files.size > 0) {
                return@fromCallable GoogleDriveFileHolder(result.files[0].id,
                        result.files[0].name,
                        result.files[0].modifiedTime,
                        result.files[0].getSize(),
                        null, null)
            } else {
                return@fromCallable GoogleDriveFileHolder()
            }
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileHolder>())
    }

    fun downloadConvertedFileToString(fileId: String): Observable<GoogleDriveResponseHolder> {
        return Observable.fromCallable {
            val outputStream = ByteArrayOutputStream()
            mDriveService.files()
                    .export(fileId, TYPE_TEXT_PLAIN)   // Retrieve the data as a plain text;
                    .executeMediaAndDownloadTo(outputStream)
            return@fromCallable GoogleDriveResponseHolder(fileId, outputStream)
        }.compose(SchedulerUtils.ioToMain<GoogleDriveResponseHolder>())
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