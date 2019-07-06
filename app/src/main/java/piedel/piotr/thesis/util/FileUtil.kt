package piedel.piotr.thesis.util

import android.graphics.Bitmap
import android.os.Environment
import piedel.piotr.thesis.data.model.receipt.Receipt
import timber.log.Timber
import java.io.*

//it has to be external storage because access to internal storage can be realized only with Context.getFilesDir()
// App has no permissions to write on system directories eg. Environment.getDataDirectory() will return system dir
private fun getDirectoryOfApp(): File {
    return File(Environment.getExternalStorageDirectory().toString() + "/piedel_piotr_thesis")

}

//@Throws(URISyntaxException::class)
//fun getPath(context: Context, uri: Uri): String? {
//    if ("content".equals(uri.scheme, ignoreCase = true)) {
//        val projection = arrayOf("_data")
//        val cursor: Cursor?
//        try {
//            cursor = context.contentResolver.query(uri, projection, null, null, null)
//            val columnIndex = cursor?.getColumnIndexOrThrow("_data")
//            if (cursor.moveToFirst()) {
//                return cursor.getString(columnIndex as Int)
//            }
//            cursor?.close()
//        } catch (e: Exception) {
//            Timber.d("%s", e.toString())
//        }
//    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//        return uri.path
//    }
//    return null
//}

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
    val filename = receipt.id.toString()
    saveImageFile(bitmap, filename)
    return filename
}

fun saveBitmapReturnOnlyPathToFile(sourcePathToFile: String?, bitmap: Bitmap): String {
    Timber.d("saveBitmapReturnOnlyPathToFile: %s", sourcePathToFile.toString())
    val pairIsFileSavedAndPath = saveImageFileReturnPathToSavedImage(bitmap, sourcePathToFile ?: "")
    return if (pairIsFileSavedAndPath.first)
        pairIsFileSavedAndPath.second
    else ""
}

fun saveImageFileReturnPathToSavedImage(bitmap: Bitmap, fileName: String): Pair<Boolean, String> {
    return Pair(saveImageFile(bitmap, fileName), getPathToImageFile(fileName))
}

fun saveImageFile(bitmap: Bitmap, fileName: String): Boolean {
    val directory = getDirectoryOfApp()
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val filePath = getPathToImageFile(fileName)
    val out: FileOutputStream
    val file = File(filePath)
    if (file.exists()) {
        file.delete()
    }
    return try {
        out = FileOutputStream(file.path)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        out.flush()
        out.close()
        true
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        false
    }
}

private fun getPathToImageFile(fileName: String) =
        getDirectoryOfApp().absolutePath + "/" + fileName + ".jpg"

fun saveFile(file: File, content: String?): Boolean {
//    val path: String = file.path
    if (content.isNullOrEmpty()) {
        return false
    }
    val directory = getDirectoryOfApp()
    if (!directory.exists()) {
        directory.mkdirs()
    }
    if (file.exists())
        file.delete()
    val fileWriter = FileWriter(file)
    try {
        fileWriter.write(content)
        return true
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
        throw RuntimeException("IOException occurred. ", e)
    } finally {
        fileWriter.close()
    }
    return file.exists()
}

fun saveFile(filePath: String, content: String?): Boolean {
    return saveFile(File(filePath), content)
}