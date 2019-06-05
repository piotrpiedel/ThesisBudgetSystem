package piedel.piotr.thesis.ui.fragment.importexport.importfromhtml

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import droidninja.filepicker.FilePickerBuilder
import piedel.piotr.thesis.R
import piedel.piotr.thesis.ui.base.BaseFragment
import javax.inject.Inject


class ImportExportFragment : BaseFragment(), ImportExportContract.ImportExportView {

    @BindView(R.id.import_file_localization)
    lateinit var importFileLocation: EditText

    override val layout: Int
        get() = R.layout.fragment_import_export

    override val toolbarTitle: String
        get() = context?.getString(R.string.importexport).toString()

    @Inject
    lateinit var importExportPresenter: ImportExportPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        importExportPresenter.attachView(this)
    }

    @OnClick(R.id.fragment_import_export_button_load)
    fun onLoadButtonClicked() {
        checkPermissionsAndOpenFilePicker()
    }

    @OnClick(R.id.fragment_import_export_button_export)
    fun onExportButtonClicked() {
    }

    private fun checkPermissionsAndOpenFilePicker() {
        importExportPresenter.checkPermissions(requireActivity())
    }

    override fun showFileChooser() {
        val htmlTypes: Array<String> = arrayOf(".htm", ".html");
        FilePickerBuilder
                .instance.setMaxCount(1)
                .setActivityTheme(R.style.LibAppTheme)
                .enableDocSupport(false)
                .addFileSupport("HTML", htmlTypes)
                .pickFile(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        importExportPresenter.handleOnActivityResult(requestCode, resultCode, data)
    }

    override fun fillEditTextWithPath(path: String?) {
        path?.let {
            importFileLocation.setText(path)
        }
    }

    override fun fillTextViewWithContent(content: String) {
    }

    override fun showInsertCompleteToast() {
        Toast.makeText(context, getString(R.string.loading_operation_from_html_success), Toast.LENGTH_SHORT).show()
    }

    override fun showError() {
        Toast.makeText(context, getString(R.string.something_went_wrong_try_again_later), Toast.LENGTH_SHORT).show()
    }


    companion object {
        const val FRAGMENT_TAG: String = "importExportFragment"
    }
}
