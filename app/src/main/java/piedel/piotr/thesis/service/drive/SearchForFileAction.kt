package piedel.piotr.thesis.service.drive

import com.google.api.services.drive.Drive
import io.reactivex.Maybe
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber

class SearchForFileAction(private val googleDriveClient: Drive)  {

    fun searchFileGenericFunction(fileName: String, mimeType: String): Maybe<GoogleDriveFileMetadataHolder> {
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
}