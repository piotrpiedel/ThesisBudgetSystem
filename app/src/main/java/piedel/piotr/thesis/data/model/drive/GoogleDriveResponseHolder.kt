package piedel.piotr.thesis.data.model.drive

import java.io.ByteArrayOutputStream

data class GoogleDriveResponseHolder(val fileId: String, val responseByteArrayOutputStream: ByteArrayOutputStream?) {

    var plainTextFromOutputStream: String = responseByteArrayOutputStream?.toByteArray()?.let { String(it) }
            ?: "The response is null"
        private set

    override fun toString(): String {
        return plainTextFromOutputStream
    }
}