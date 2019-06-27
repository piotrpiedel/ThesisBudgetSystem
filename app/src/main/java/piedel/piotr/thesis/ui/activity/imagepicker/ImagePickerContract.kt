package piedel.piotr.thesis.ui.activity.imagepicker

import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter
import piedel.piotr.thesis.util.listener.CameraAndStoragePermissionListener

interface ImagePickerContract {
    interface ImagePickerView : BaseView, CameraAndStoragePermissionListener.CameraAndStorageViewInterface {

    }

    interface PresenterContract<T : BaseView> : Presenter<T> {

        fun openPickerBasedOnGivenPermissions(imagePickerActivity: ImagePickerActivity)

    }
}