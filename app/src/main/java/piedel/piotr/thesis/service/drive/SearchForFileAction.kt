package piedel.piotr.thesis.service.drive

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.FileList
import io.reactivex.Maybe
import io.reactivex.Observable
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber

class SearchForFileAction(private val googleDriveClient: Drive) {

    fun searchForFileFunction(fileName: String, mimeType: String): Maybe<GoogleDriveFileMetadataHolder> {
        return Maybe.fromCallable {
            val pageToken: String? = null
            val result = googleDriveClient.files().list()
                    .setQ("name = '$fileName' and mimeType ='$mimeType'")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute()
            for (file in result.files) {
                Timber.d("Found file: %s (%s)\n", file.name, file.id)
            }
            if (result.files.size > 0) {
                return@fromCallable GoogleDriveFileMetadataHolder(result.files[0].id, result.files[0].name,
                        result.files[0].modifiedTime, result.files[0].getSize(), null, null)
            } else {
                return@fromCallable GoogleDriveFileMetadataHolder()
            }
        }.compose(SchedulerUtils.ioToMain<GoogleDriveFileMetadataHolder>())
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
}