package piedel.piotr.thesis.ui.fragment.receipt.receiptadd

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jakewharton.rxbinding3.widget.textChanges
import com.karumi.dexter.Dexter
import droidninja.filepicker.FilePickerConst
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.data.model.receipt.ReceiptRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.receipt.receiptadd.ReceiptAddContract.PresenterContract
import piedel.piotr.thesis.ui.fragment.receipt.receiptadd.ReceiptAddContract.ReceiptAddView
import piedel.piotr.thesis.util.glide.glideLoadAsBitmap
import piedel.piotr.thesis.util.listener.CameraAndStoragePermissionListener
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@ConfigPersistent
class ReceiptAddPresenter @Inject constructor(private val receiptRepository: ReceiptRepository) : BasePresenter<ReceiptAddView>(), PresenterContract<ReceiptAddView> {

    private var disposable: Disposable? = null

    override fun initFragment(receipt: Receipt?) {
        checkViewAttached()
        loadReceiptData(receipt)
        view?.setOnCalendarClickListener()
        view?.checkPermissions()
    }

    private fun loadReceiptData(receipt: Receipt?) {
        receipt?.let {
        } ?: return
    }

    override fun checkPermissionsForStorageAndCamera(passedActivityFragment: FragmentActivity) {
        val permissionsList: List<String> = listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        Dexter.withActivity(passedActivityFragment)
                .withPermissions(permissionsList)
                .withListener(CameraAndStoragePermissionListener(view))
                .check()
    }

    override fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?, passedActivity: FragmentActivity) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            handlePickingFileResult(data, passedActivity)
        }
    }

    private fun handlePickingFileResult(data: Intent, passedActivity: FragmentActivity) {
        val stringPath = data.extras?.getStringArrayList(FilePickerConst.KEY_SELECTED_MEDIA) // using KEY_SELECTED_MEDIA return Array<String>
        loadPictureFromGallery(passedActivity, stringPath?.first())
    }

    override fun loadPictureFromGallery(fragmentActivity: FragmentActivity, picturePath: String?) {
        glideLoadAsBitmap(fragmentActivity, picturePath)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        Timber.d("onLoadFailed${e.toString()}")
                        return true
                    }

                    override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Timber.d("onResourceReady")
                        view?.setReceiptImageFromResource(resource)
                        return true
                    }
                })
                .submit()
    }

    override fun onSaveOperationButtonClicked() {
        view?.showProgressBar(true)
        view?.startCreatingReceipt()
    }

    private fun insertReceiptStart(receipt: Receipt) {
        disposable = receiptRepository.insertReceipt(receipt)
                .subscribe(
                        {
                            receipt.id = it.toInt()
                            Timber.d("OnSucces: checkIfOperationExist onSucces update and return ")
                            view?.createReceipt(it, receipt)
                        },
                        { t ->
                            Timber.d("OnError: checkIfOperationExist %s", t.localizedMessage)
                        }
                )
        addDisposable(disposable)
    }

    override fun updateReceipt(receipt: Receipt) {
        receiptRepository.updateReceipt(receipt)
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        view?.returnFromFragment()
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }
                })
    }

    override fun generateEmptyReceipt() {
        val receipt = Receipt()
        insertReceiptStart(receipt)
    }

    override fun observeTheInputValue(titleEditText: EditText, dateTextView: TextView, valueEditText: EditText) {
        val titleObservable: Observable<CharSequence> = observableBuilder(titleEditText)
        val valueObservable: Observable<CharSequence> = observableBuilder(valueEditText)
        val dateObservable: Observable<CharSequence> = observableBuilder(dateTextView)
        disposable = Observables.combineLatest(titleObservable, valueObservable, dateObservable,
                { title, value, date -> title.isNotEmpty() && value.isNotEmpty() && date.isNotEmpty() })
                .debounce(400, TimeUnit.MILLISECONDS, mainThread())
                .compose(SchedulerUtils.ioToMain())
                .subscribe({
                    view?.enableSaveButton(it)
                }, {
                    Timber.d("observeTheInputValue  ${it.localizedMessage}")

                }, {
                    Timber.d("observeTheInputValue  onComplete")
                })
        addDisposable(disposable)
    }

    private fun observableBuilder(textView: TextView): Observable<CharSequence> {
        return textView
                .textChanges().skip(1)
                .debounce(400, TimeUnit.MILLISECONDS)
                .compose(SchedulerUtils.ioToMain())
    }
}