package piedel.piotr.thesis.service.drive

import com.google.api.services.drive.Drive
import io.reactivex.Single
import piedel.piotr.thesis.configuration.DEFAULT_GOOGLE_DRIVE_APP_UPLOAD_FOLDER_NAME
import piedel.piotr.thesis.configuration.TYPE_GOOGLE_DRIVE_FOLDER
import piedel.piotr.thesis.data.model.drive.GoogleDriveFileMetadataHolder

class SearchForFolderAction(private val googleDriveClient: Drive) {

    fun searchForAppRootFolder(): Single<GoogleDriveFileMetadataHolder> {
        val searchForFolderAction = SearchForFileAction(googleDriveClient)
        return searchForFolderAction
                .searchForFileFunction(DEFAULT_GOOGLE_DRIVE_APP_UPLOAD_FOLDER_NAME, TYPE_GOOGLE_DRIVE_FOLDER)
    }
}