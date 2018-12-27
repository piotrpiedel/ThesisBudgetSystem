package piedel.piotr.thesis.ui.fragment.importexport.importfromhtml

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.getPath
import piedel.piotr.thesis.util.parseHTMLFileToJsonArray
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import javax.inject.Inject


@ConfigPersistent
class ImportExportPresenter @Inject constructor(private val operationsRepository: OperationRepository) : BasePresenter<ImportExportView>() {


    private fun insertOperation(vararg operation: Operation) {
        Completable.fromAction { operationsRepository.insertOperation(*operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
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

    fun checkPermissions(fragmentActivity: FragmentActivity) {
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

    fun resultFromRequestPermission(requestCode: Int, grantResults: IntArray) {
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

    fun getFilePathFromResult(requestCode: Int, resultCode: Int, data: Intent?, fragmentActivity: FragmentActivity) {
        when (requestCode) {
            ImportExportFragment.FILE_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data    // Get the Uri of the selected file
                val path = getPath(fragmentActivity, uri as Uri) // Get the FilePath of the selected file
                fillEditTextWithPathOfFile(path) // Fill the EditText with FilePath
                parseHTMLFromPath(path)

            }
        }
    }

    fun parseHTMLFromPath(path: String?) {
        var jsonArray: JSONArray? = null
        path?.let {
            jsonArray = parseHTMLFileToJsonArray(path = path)
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