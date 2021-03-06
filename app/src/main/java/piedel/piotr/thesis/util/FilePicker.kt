package piedel.piotr.thesis.util

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import droidninja.filepicker.FilePickerBuilder
import piedel.piotr.thesis.R


fun getImageFilePicker(context: Activity?, enableCamera: Boolean, requestCode: Int) {
    if (context is FragmentActivity)
        filePickerBuilder(enableCamera).pickPhoto(context, requestCode)
    else if (context is Activity)
        filePickerBuilder(enableCamera).pickPhoto(context, requestCode)
}

fun getImageFilePicker(context: Fragment?, enableCamera: Boolean, requestCode: Int) {
    if (context is Fragment)
        filePickerBuilder(enableCamera).pickPhoto(context, requestCode)
}


//TODO: probably refactor because it wont work;
fun getImageFilePicker(context: Context?, enableCamera: Boolean) {
    if (context is Fragment)
        filePickerBuilder(enableCamera).pickPhoto(context)
    else if (context is Activity)
        filePickerBuilder(enableCamera).pickPhoto(context)
}

private fun filePickerBuilder(enableCamera: Boolean): FilePickerBuilder {
    return FilePickerBuilder
            .instance
            .setMaxCount(1)
            .setActivityTheme(R.style.AppTheme)
            .enableCameraSupport(enableCamera)
}

