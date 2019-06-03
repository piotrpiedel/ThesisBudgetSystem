package piedel.piotr.thesis.data.model.drive

import com.google.api.client.util.DateTime
import java.util.*


data class GoogleDriveFileHolder(var id: String?, var name: String?, var modifiedTime: DateTime?,
                                 var size: Long?, var createdTime: DateTime?, var starred: Boolean?) {

    constructor(id: String?, name: String?) : this(id, name, null, null, null, null)

    constructor() : this("0", "Null path", DateTime(Date()), 0, DateTime(Date()), false)

}