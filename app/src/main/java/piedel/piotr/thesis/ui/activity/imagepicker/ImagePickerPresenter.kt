package piedel.piotr.thesis.ui.activity.imagepicker

import android.Manifest
import com.karumi.dexter.Dexter
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.activity.imagepicker.ImagePickerContract.ImagePickerView
import piedel.piotr.thesis.ui.activity.imagepicker.ImagePickerContract.PresenterContract
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.listener.CameraAndStoragePermissionListener
import javax.inject.Inject

@ConfigPersistent
class ImagePickerPresenter @Inject constructor() : BasePresenter<ImagePickerView>(), PresenterContract<ImagePickerView> {

    override fun openPickerBasedOnGivenPermissions(imagePickerActivity: ImagePickerActivity) {
        Dexter.withActivity(imagePickerActivity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(CameraAndStoragePermissionListener(view))
                .check()
    }
}