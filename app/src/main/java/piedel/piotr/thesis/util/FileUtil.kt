package piedel.piotr.thesis.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import timber.log.Timber
import java.net.URISyntaxException

@Throws(URISyntaxException::class)
fun getPath(context: Context, uri: Uri): String? {
    if ("content".equals(uri.scheme, ignoreCase = true)) {
        val projection = arrayOf("_data")
        val cursor: Cursor?
        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow("_data")
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex as Int)
            }
            cursor?.close()
        } catch (e: Exception) {
            Timber.d("" + e.toString())
        }
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}