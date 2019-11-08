package piedel.piotr.thesis.service.drive

import com.google.api.services.drive.Drive
import io.reactivex.Observable
import io.reactivex.Single
import piedel.piotr.thesis.configuration.TYPE_TEXT_PLAIN
import piedel.piotr.thesis.data.model.drive.GoogleDriveResponseHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class DownloadFileAction(private val googleDriveClient: Drive) {

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
}