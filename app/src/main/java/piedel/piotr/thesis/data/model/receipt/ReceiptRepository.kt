package piedel.piotr.thesis.data.model.receipt

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import javax.inject.Inject

class ReceiptRepository @Inject constructor(private val receiptDao: ReceiptDao) {

    fun insertReceipt(receipt: Receipt): Single<Long> {
        return Single.fromCallable { receiptDao.insertReceipt(receipt) }
                .compose(SchedulerUtils.ioToMain<Long>())
    }

    fun insertReceipt(vararg receipt: Receipt) {
        Completable.fromAction { receiptDao.insertReceipt(*receipt) }
                .compose(SchedulerUtils.ioToMain<Receipt>())
                .subscribe(CompletableObserverMain())
    }

    fun updateReceipt(receipt: Receipt) {
        Completable.fromAction { receiptDao.updateReceipt(receipt) }
                .compose(SchedulerUtils.ioToMain<Receipt>())
                .subscribe(CompletableObserverMain())
    }

    fun deleteReceipt(receipt: Receipt) {
        Completable.fromAction { receiptDao.deleteReceipt(receipt) }
                .compose(SchedulerUtils.ioToMain<Receipt>())
                .subscribe(CompletableObserverMain())
    }

    fun selectReceipt(idReceiptOther: Int): Maybe<Receipt> {
        return receiptDao.selectReceipt(idReceiptOther)
                .compose(SchedulerUtils.ioToMain<Receipt>())
    }

    fun selectAllReceipts(): Maybe<MutableList<Receipt>> {
        return receiptDao.selectAllReceipts()
                .compose(SchedulerUtils.ioToMain<MutableList<Receipt>>())
    }

    @SuppressLint("CheckResult")
    fun deleteAllReceipts() {
        Completable.fromAction { receiptDao.deleteAllReceipts() }
                .compose(SchedulerUtils.ioToMain<Receipt>())
    }
}