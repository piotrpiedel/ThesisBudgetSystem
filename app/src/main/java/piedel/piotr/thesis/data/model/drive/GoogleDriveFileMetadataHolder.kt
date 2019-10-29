package piedel.piotr.thesis.data.model.drive

import com.google.api.client.util.DateTime


data class GoogleDriveFileMetadataHolder(var id: String?, var name: String?, var modifiedTime: DateTime?,
                                         var size: Long?, var createdTime: DateTime?, var starred: Boolean?) {

    constructor(id: String?, name: String?) : this(id, name, null, null, null, null)

    constructor() : this(null, null, null, null, null, null)

}