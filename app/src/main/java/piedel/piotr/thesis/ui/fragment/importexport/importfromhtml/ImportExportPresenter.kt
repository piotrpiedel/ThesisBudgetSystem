package piedel.piotr.thesis.ui.fragment.importexport.importfromhtml

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.squareup.moshi.Moshi
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.importexport.importfromhtml.ImportExportContract.ImportExportView
import piedel.piotr.thesis.ui.fragment.importexport.importfromhtml.ImportExportContract.PresenterContract
import piedel.piotr.thesis.util.getPath
import piedel.piotr.thesis.util.parseHTMLFileToJsonArray
import piedel.piotr.thesis.util.showToast
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@ConfigPersistent
class ImportExportPresenter @Inject constructor(private val operationsRepository: OperationRepository) : BasePresenter<ImportExportView>(), PresenterContract<ImportExportView> {


    private fun insertOperation(vararg operation: Operation) {
        operationsRepository.insertOperation(*operation)
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        view?.showInsertCompleteToast()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }
                })
    }

    override fun checkPermissions(fragmentActivity: FragmentActivity) {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(fragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission)) {
                view?.showError()
            } else {
                ActivityCompat.requestPermissions(fragmentActivity, arrayOf(permission), ImportExportFragment.PERMISSIONS_REQUEST_CODE)
            }
        } else {
            view?.showFileChooser()
        }
    }

    override fun resultFromRequestPermission(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            ImportExportFragment.PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    view?.showFileChooser()
                } else {
                    view?.showError()
                }
            }
        }
    }

    override fun getFilePathFromResult(requestCode: Int, resultCode: Int, data: Intent?, fragmentActivity: FragmentActivity) {
        when (requestCode) {
            ImportExportFragment.FILE_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data    // Get the Uri of the selected file
                val path = getPath(fragmentActivity, uri as Uri) // Get the FilePath of the selected file
                fillEditTextWithPathOfFile(path) // Fill the EditText with FilePath
                createAndParseHTMLFileFromPath(path, fragmentActivity)
            }
        }
    }

    override fun createAndParseHTMLFileFromPath(path: String?, fragmentActivity: FragmentActivity) {
        val fileToParse: File?
        if (path != null) {
            fileToParse = File(path)
            if (fileToParse.exists() && checkIfFileIsHtml(fileToParse)) {
                parseHTMLFromPath(fileToParse)
            } else {
                showToast(fragmentActivity, fragmentActivity.getString(R.string.file_you_want_to_import_must_be_html))
            }
        } else {
            showToast(fragmentActivity, fragmentActivity.getString(R.string.to_import_file_you_should_use_file_manager))
        }
    }

    private fun checkIfFileIsHtml(fileToParse: File): Boolean {
        return fileToParse.toString().toLowerCase().endsWith(".htm") || fileToParse.toString().toLowerCase().endsWith(".html")
    }

    private fun parseHTMLFromPath(fileToParse: File) {
        var jsonArray: JSONArray?
        fileToParse.let {
            jsonArray = parseHTMLFileToJsonArray(fileToParse)
        }
        jsonArray.let {
            if (jsonArray?.length() != 0)
                createOperationsFromJson(jsonArray)
            else view?.showError()
        }
    }

    private fun createOperationsFromJson(jsonArray: JSONArray?) {

        val moshiBuilder = Moshi.Builder().add(Operation::class.java, OperationClassAdapter())
        val moshi = moshiBuilder.build()
        val jsonAdapter = moshi.adapter(Operation::class.java)
        val arrayOfOperations: MutableList<Operation> = mutableListOf()
        for (iterator in 0 until jsonArray?.length() as Int) {
            val operationObject = jsonArray.getJSONObject(iterator).toString()
            val operation = jsonAdapter.fromJson(operationObject)
            arrayOfOperations.add(operation as Operation)
        }
        insertOperation(*arrayOfOperations.toTypedArray())
    }

    private fun fillEditTextWithPathOfFile(path: String?) {
        view?.fillEditTextWithPath(path)
    }

}