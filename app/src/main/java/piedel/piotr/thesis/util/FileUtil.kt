package piedel.piotr.thesis.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import piedel.piotr.thesis.data.model.receipt.Receipt
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.URISyntaxException

private fun getDirectoryOfApp(): File {
    return File(Environment.getExternalStorageDirectory().toString() + "/Thesis")
}

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
            Timber.d("%s", e.toString())
        }
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

/**
 * Calling this will delete the images from cache directory
 * useful to clear some memory
 */
//This one is interesting to clear data after taking photo
fun clearTemporaryFiles(paths: List<String>) {
    for (path in paths) {
        val fileFromPath = File(path)
        if (fileFromPath.exists() && fileFromPath.isFile) {
            fileFromPath.deleteOnExit()
        }
    }
}

fun saveImageFile(bitmap: Bitmap, receipt: Receipt): String {
    val out: FileOutputStream?
    val directory = getDirectoryOfApp()
    if (!directory.exists()) {
        directory.mkdirs()
    }

    val filename = directory.absolutePath + "/" + receipt.id.toString() + ".jpg"
    val file = File(filename)
    if (file.exists())
        file.delete()
    try {
        out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        out.flush();
        out.close();
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    return filename
}
