package piedel.piotr.thesis.ui.fragment.receipt.receiptadd

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.FragmentActivity
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
import piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog.ChoosePictureSourceDialog
import piedel.piotr.thesis.util.choosePictureSourceDialogRequestCode
import piedel.piotr.thesis.util.getCircularProgressDrawable
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
        } ?: return
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?, passedActivity: FragmentActivity) {
        if (requestCode == choosePictureSourceDialogRequestCode && resultCode == Activity.RESULT_OK) {
            if (data?.hasExtra(ChoosePictureSourceDialog.INTENT_WITH_PICTURE_FROM_CAMERA) == true) {
                val passedData = data.getParcelableExtra<Intent>(ChoosePictureSourceDialog.INTENT_WITH_PICTURE_FROM_CAMERA)
                getPictureFromCamera(passedData)

            } else if (data?.hasExtra(ChoosePictureSourceDialog.PICTURE_PATH) == true) {
                val passedData = data.getStringExtra(ChoosePictureSourceDialog.PICTURE_PATH)
                loadPictureFromGallery(passedActivity, passedData)

            }
        }
    }

    private fun getPictureFromCamera(data: Intent?) {
        val thumbnail = data?.extras?.get("data") as Bitmap
        view?.setReceiptImageFromResource(thumbnail)
    }

    @SuppressLint("CheckResult")
    fun loadPictureFromGallery(fragmentActivity: FragmentActivity, picturePath: String?) {
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
}