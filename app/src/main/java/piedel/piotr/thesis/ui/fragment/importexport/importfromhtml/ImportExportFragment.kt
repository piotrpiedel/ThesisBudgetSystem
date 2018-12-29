package piedel.piotr.thesis.ui.fragment.importexport.importfromhtml

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import io.reactivex.annotations.NonNull
import piedel.piotr.thesis.R
import piedel.piotr.thesis.ui.base.BaseFragment
import javax.inject.Inject


class ImportExportFragment : BaseFragment(), ImportExportView {

    @BindView(R.id.import_file_localization)
    lateinit var importFileLocation: EditText

    override val layout: Int
        get() = R.layout.fragment_import_export

    override val toolbarTitle: String
        get() = FRAGMENT_TITLE

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
        if (importFileLocation.text.isNotBlank())
            importExportPresenter.parseHTMLFromPath(importFileLocation.text.toString())
    }

    private fun checkPermissionsAndOpenFilePicker() {
        importExportPresenter.checkPermissions(requireActivity())
    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        importExportPresenter.resultFromRequestPermission(requestCode, grantResults)
    }

    override fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to open"), FILE_SELECT_CODE)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, ex.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        importExportPresenter.getFilePathFromResult(requestCode, resultCode, data, requireActivity())
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun fillEditTextWithPath(path: String?) {
        path?.let {
            importFileLocation.setText(path)
        }
    }

    override fun fillTextViewWithContent(content: String) {
    }

    override fun showInsertCompleteToast() {
        Toast.makeText(context, "Loading operation from HTML succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun showError() {
        Toast.makeText(context, "Something went wrong, try again later", Toast.LENGTH_SHORT).show()
    }


    companion object {
        const val FRAGMENT_TAG: String = "importExportFragment"
        const val FRAGMENT_TITLE: String = " Import | Export "
        const val PERMISSIONS_REQUEST_CODE = 80
        const val FILE_SELECT_CODE = 81
    }
}
