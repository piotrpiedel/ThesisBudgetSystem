package piedel.piotr.thesis.ui.fragment.receipt.receiptadd

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.data.model.receipt.ReceiptRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog.ImageSourceOptions
import piedel.piotr.thesis.util.getCircularProgressDrawable
import piedel.piotr.thesis.util.getPath
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class ReceiptAddPresenter @Inject constructor(private val receiptRepository: ReceiptRepository) : BasePresenter<ReceiptAddView>() {

    private var disposable: Disposable? = null

    fun initFragment(receipt: Receipt?) {
        checkViewAttached()
        loadReceiptData(receipt)
        view?.setOnCalendarClickListener()
    }

    fun initChooseDialog() {
        checkViewAttached()
        view?.setOnCalendarClickListener()
        view?.showChooseDialog()
    }

    private fun loadReceiptData(receipt: Receipt?) {
        receipt?.let {
            //            view?.fillTheData(receipt)
        } ?: return
    }

    fun onLoadFromGalleryClick(passedActivityFragment: FragmentActivity) {
        checkPermissionsForStorage(passedActivityFragment)
    }

    private fun checkPermissionsForStorage(fragmentActivity: FragmentActivity) {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(fragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission)) {
                view?.showError()//???
            } else {
                ActivityCompat.requestPermissions(fragmentActivity, arrayOf(permission), ReceiptAddFragment.PERMISSIONS_REQUEST_CODE)
            }
        } else {
            view?.showFileChooser()
        }
    }

    fun resultFromRequestPermission(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            ReceiptAddFragment.PERMISSIONS_REQUEST_CODE -> {
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
            ReceiptAddFragment.FILE_SELECT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data    // Get the Uri of the selected file
                val picturePath = getPath(fragmentActivity, uri as Uri) // Get the FilePath of the selected file
                loadPictureFromGallery(fragmentActivity, picturePath)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadPictureFromGallery(fragmentActivity: FragmentActivity, picturePath: String?) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(getCircularProgressDrawable(fragmentActivity))
        requestOptions.error(R.drawable.ic_outline_error_outline)
        Glide.with(fragmentActivity)
                .asBitmap()
                .load(picturePath)
                .apply(requestOptions)
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

    fun onSaveOperationButtonClicked() {
        view?.showProgressBar(true)
        view?.startCreatingReceipt()
    }

    private fun insertReceiptStart(receipt: Receipt) {
        disposable = receiptRepository.insertReceipt(receipt)
                .compose(SchedulerUtils.ioToMain<Long>())
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

    fun onLoadFromCameraClick(requireActivity: FragmentActivity) {
        checkPermissionsForCamera(requireActivity)
    }

    private fun checkPermissionsForCamera(fragmentActivity: FragmentActivity) {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(fragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission)) {
                view?.showError()//???
            } else {
                ActivityCompat.requestPermissions(fragmentActivity, arrayOf(permission), ReceiptAddFragment.PERMISSIONS_REQUEST_CODE)
            }
        } else {
            view?.showCamera()
        }
    }

    fun getPictureFromCamera(requestCode: Int, resultCode: Int, data: Intent?) {
        val thumbnail = data?.extras?.get("data") as Bitmap
        view?.setReceiptImageFromResource(thumbnail)
    }

    fun updateReceipt(receipt: Receipt) {
        Completable.fromAction { receiptRepository.updateReceipt(receipt) }
                .compose(SchedulerUtils.ioToMain<Receipt>())
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

    fun generateEmptyReceipt() {
        val receipt = Receipt()
        insertReceiptStart(receipt)
    }

    fun switchCameraMemorySource(choosePictureSourceDialog: ImageSourceOptions, passedActivityContext: FragmentActivity) {
        when (choosePictureSourceDialog) {
            ImageSourceOptions.INTERNAL_MEMORY -> onLoadFromGalleryClick(passedActivityContext)
            ImageSourceOptions.CAMERA -> onLoadFromCameraClick(passedActivityContext)
            ImageSourceOptions.NOT_RECOGNIZED_SOURCE -> view?.showError()
        }
    }


}