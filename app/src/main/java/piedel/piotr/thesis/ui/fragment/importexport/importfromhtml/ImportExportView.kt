package piedel.piotr.thesis.ui.fragment.importexport.importfromhtml

import piedel.piotr.thesis.ui.base.BaseView

interface ImportExportView : BaseView {

    fun showError()

    fun showFileChooser()

    fun fillEditTextWithPath(path: String?)

    fun fillTextViewWithContent(content: String)

    fun showInsertCompleteToast()
}