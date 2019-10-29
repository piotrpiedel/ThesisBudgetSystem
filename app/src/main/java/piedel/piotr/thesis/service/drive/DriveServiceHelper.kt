package piedel.piotr.thesis.service.drive

import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import io.reactivex.Observable
import io.reactivex.Single
import piedel.piotr.thesis.configuration.*
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
class DriveServiceHelper(private val googleDriveClient: Drive) {

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */

    fun createTextFile(fileName: String, content: String, folderId: String?): Observable<GoogleDriveFileMetadataHolder> {
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

            val googleFile = googleDriveClient.files().create(metadata, contentStream).execute()
                    ?: throw IOException("Null result when requesting file creation.")
            val googleDriveFileHolder = GoogleDriveFileMetadataHolder()
            googleDriveFileHolder.id = googleFile.id
            return@fromCallable googleDriveFileHolder
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())
    }


    fun createAndUploadImageFile(fileName: String, content: String, folderId: String?): Observable<GoogleDriveFileMetadataHolder> {
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

            val googleFile = googleDriveClient.files().create(metadata, contentStream).execute()
                    ?: throw IOException("Null result when requesting file creation.")
            val googleDriveFileHolder = GoogleDriveFileMetadataHolder()
            googleDriveFileHolder.id = googleFile.id
            return@fromCallable googleDriveFileHolder
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())
    }

    /**
     * Opens the file identified by `fileId` and returns a [Pair] of its name and
     * contents.
     */
    fun readFile(fileId: String): Observable<Pair<String, String>> {
        return Observable.fromCallable {
            // Retrieve the metadata as a File object.
            val metadata = googleDriveClient.files().get(fileId).execute()
            val name = metadata.name

            // Stream the file contents to a String.
            googleDriveClient.files().get(fileId).executeMediaAsInputStream().use { inputStream ->
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
            googleDriveClient.files().update(fileId, metadata, contentStream).execute()
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
            return@fromCallable googleDriveClient.files().list().setSpaces("drive").execute()
        }.compose(SchedulerUtils.ioToMain<FileList>())
    }

    fun downloadConvertedFileToString(fileId: String): Single<GoogleDriveResponseHolder> {
        return Single.fromCallable {
            val outputStream = ByteArrayOutputStream()
            googleDriveClient.files()
                    .export(fileId, TYPE_TEXT_PLAIN)   // Retrieve the data as a plain text;
                    .executeMediaAndDownloadTo(outputStream)
            return@fromCallable GoogleDriveResponseHolder(outputStream)
        }
                .timeout(20, TimeUnit.SECONDS)
                .compose(SchedulerUtils.ioToMain<GoogleDriveResponseHolder>())
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