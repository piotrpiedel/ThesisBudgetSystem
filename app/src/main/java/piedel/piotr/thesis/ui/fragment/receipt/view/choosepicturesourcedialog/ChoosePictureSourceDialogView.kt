package piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog

import android.content.Intent
import piedel.piotr.thesis.ui.base.BaseView

interface ChoosePictureSourceDialogView : BaseView {

    fun showFileChooser()

    fun showCamera()

    fun passPicturePath(picturePath: String)

    fun passIntentWithPicture(data: Intent?)

    fun onPermissionDenied()

}