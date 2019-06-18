package piedel.piotr.thesis.ui.fragment.importexport.importfromhtml

import android.Manifest
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.moshi.Moshi
import droidninja.filepicker.FilePickerConst
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.importexport.importfromhtml.ImportExportContract.ImportExportView
import piedel.piotr.thesis.ui.fragment.importexport.importfromhtml.ImportExportContract.PresenterContract
import piedel.piotr.thesis.util.parseHTMLFileToJsonArray
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
        checkPermissionForStorage(fragmentActivity)
    }

    private fun checkPermissionForStorage(fragmentActivity: FragmentActivity) {
        val permissions: String = Manifest.permission.READ_EXTERNAL_STORAGE
        Dexter.withActivity(fragmentActivity)
                .withPermission(permissions)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        view?.showFileChooser()
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                        view?.showError()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        view?.showError()
                    }
                })
                .check()
    }

    override fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val stringPath = data?.extras?.getStringArrayList(FilePickerConst.KEY_SELECTED_DOCS)?.first() // using KEY_SELECTED_MEDIA return Array<String>
        fillEditTextWithPathOfFile(stringPath) // Fill the EditText with FilePath
        createOperationsFromImportedFile(stringPath)
    }

    private fun createOperationsFromImportedFile(stringPath: String?) {
        val fileToParse = createFileFromPath(stringPath)
        val jsonArray: JSONArray?

        if (fileToParse?.exists() == true) {
            jsonArray = parseHTMLFileToArrayJson(fileToParse)
        } else return

        checkIfArrayCorrectAndCreateOperations(jsonArray)
    }

    private fun createFileFromPath(path: String?): File? {
        path?.let { return File(path) } ?: return null
    }

    private fun parseHTMLFileToArrayJson(fileToParse: File): JSONArray? {
        return parseHTMLFileToJsonArray(fileToParse)
    }

    private fun checkIfArrayCorrectAndCreateOperations(jsonArray: JSONArray?) {
        jsonArray?.let {
            if (jsonArray.length() != 0)
                createOperationsFromJson(jsonArray)
            else view?.showError()
        }
    }

    private fun createOperationsFromJson(jsonArray: JSONArray?) {
        val moshi = Moshi.Builder().add(Operation::class.java, OperationClassAdapter()).build()
        val jsonAdapter = moshi.adapter(Operation::class.java)
        val arrayOfOperations: MutableList<Operation> = mutableListOf()
        for (iterator in 0 until jsonArray?.length() as Int) {
            val operationObject = jsonArray.getJSONObject(iterator).toString()
            val operation = jsonAdapter.fromJson(operationObject)
            arrayOfOperations.add(operation as Operation)
        }
        insertCreatedOperations(arrayOfOperations)
    }

    private fun insertCreatedOperations(arrayOfOperations: MutableList<Operation>) {
        insertOperation(*arrayOfOperations.toTypedArray())
    }

    private fun fillEditTextWithPathOfFile(path: String?) {
        view?.fillEditTextWithPath(path)
    }

}