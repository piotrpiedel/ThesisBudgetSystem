package piedel.piotr.thesis.ui.activity.imagepicker

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.yalantis.ucrop.UCrop
import piedel.piotr.thesis.data.buildier.ImagePickerActivityOptions
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter
import piedel.piotr.thesis.util.listener.CameraAndStoragePermissionListener
import java.io.File

interface ImagePickerContract {
    interface ImagePickerView : BaseView, CameraAndStoragePermissionListener.CameraAndStorageViewInterface {

    }

    interface PresenterContract<T : BaseView> : Presenter<T> {

        fun openPickerBasedOnGivenPermissions(imagePickerActivity: ImagePickerActivity)

        fun bitmapDecodeAndGrayScaleOrOriginal(originalPickedImageFile: File, context: Context): Bitmap

        fun getOriginalToGrayScaleToModifiedBitmap(sourcePathToFile: String, context: Context): Uri

        fun setOptionsFromPassedIntentOrDefault(imagePickerOptions: ImagePickerActivityOptions): UCrop.Options
    }
}