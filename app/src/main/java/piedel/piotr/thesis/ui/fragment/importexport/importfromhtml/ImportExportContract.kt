package piedel.piotr.thesis.ui.fragment.importexport.importfromhtml

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface ImportExportContract {
    interface ImportExportView : BaseView {

        fun showError()

        fun showFileChooser()

        fun fillEditTextWithPath(path: String?)

        fun fillTextViewWithContent(content: String)

        fun showInsertCompleteToast()
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun checkPermissions(fragmentActivity: FragmentActivity)

        fun resultFromRequestPermission(requestCode: Int, grantResults: IntArray)

        fun getFilePathFromResult(requestCode: Int, resultCode: Int, data: Intent?, fragmentActivity: FragmentActivity)

        fun createAndParseHTMLFileFromPath(path: String?, fragmentActivity: FragmentActivity)
    }
}