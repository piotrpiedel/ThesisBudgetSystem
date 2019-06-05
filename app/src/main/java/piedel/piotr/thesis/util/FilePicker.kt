package piedel.piotr.thesis.util

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import droidninja.filepicker.FilePickerBuilder
import piedel.piotr.thesis.R


fun getImageFilePicker(context: Context?, enableCamera: Boolean, requestCode: Int) {
    if (context is Fragment)
        filePickerBuilder(enableCamera).pickPhoto(context, requestCode)
    else if (context is Activity)
        filePickerBuilder(enableCamera).pickPhoto(context, requestCode)
}

fun getImageFilePicker(context: Context?, enableCamera: Boolean) {
    if (context is Fragment)
        filePickerBuilder(enableCamera).pickPhoto(context)
    else if (context is Activity)
        filePickerBuilder(enableCamera).pickPhoto(context)
}

private fun filePickerBuilder(enableCamera: Boolean): FilePickerBuilder {
    return FilePickerBuilder
            .instance.setMaxCount(1)
            .setActivityTheme(R.style.LibAppTheme)
            .enableCameraSupport(enableCamera)
}

