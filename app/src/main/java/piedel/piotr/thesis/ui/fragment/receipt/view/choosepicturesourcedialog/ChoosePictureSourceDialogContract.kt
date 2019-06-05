package piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter
@Deprecated("Replaced with FilePickerLibrary")
interface ChoosePictureSourceDialogContract {
    interface ChoosePictureSourceDialogView : BaseView {

        fun showFileChooser()

        fun showCamera()

        fun passPicturePath(picturePath: String)

        fun passIntentWithPicture(data: Intent?)

        fun onPermissionDenied()

        fun showErrorFileNotImage()

    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun onLoadFromGalleryClick(passedActivityFragment: FragmentActivity)

        fun onLoadFromCameraClick(passedActivityFragment: FragmentActivity)

        fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?, passedActivityFragment: FragmentActivity)
    }
}