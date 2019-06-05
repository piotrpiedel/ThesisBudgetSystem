package piedel.piotr.thesis.util

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import piedel.piotr.thesis.R


@Deprecated("Please use FilePicker library instead")
class FileChooser(val context: Activity, var fileSelectCode: Int) {
    var intent = Intent(Intent.ACTION_GET_CONTENT)

    init {
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startNewFileChooser()
    }

    private fun startNewFileChooser() {
        try {
            context.startActivityForResult(Intent.createChooser(intent, "Select a File to open"), fileSelectCode)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.please_install_file_manager), Toast.LENGTH_SHORT).show()
            Toast.makeText(context, ex.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}