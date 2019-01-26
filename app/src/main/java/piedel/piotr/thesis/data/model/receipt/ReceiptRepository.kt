package piedel.piotr.thesis.data.model.receipt

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import javax.inject.Inject

class ReceiptRepository @Inject constructor(private val receiptDao: ReceiptDao) {

    fun insertReceipt(receipt: Receipt): Single<Long> {
        return Single.fromCallable { receiptDao.insertReceipt(receipt) }
                .compose(SchedulerUtils.ioToMain<Long>())
    }

    fun insertReceipt(vararg receipt: Receipt): Completable {
        return Completable.fromAction { receiptDao.insertReceipt(*receipt) }
                .compose(SchedulerUtils.ioToMain<Receipt>())
    }

    fun updateReceipt(receipt: Receipt): Completable {
        return Completable.fromAction { receiptDao.updateReceipt(receipt) }
                .compose(SchedulerUtils.ioToMain<Receipt>())
    }

    fun deleteReceipt(receipt: Receipt): Completable {
        return Completable.fromAction { receiptDao.deleteReceipt(receipt) }
                .compose(SchedulerUtils.ioToMain<Receipt>())
    }

    fun selectReceipt(idReceiptOther: Int): Maybe<Receipt> {
        return receiptDao.selectReceipt(idReceiptOther)
                .compose(SchedulerUtils.ioToMain<Receipt>())
    }

    fun selectAllReceipts(): Maybe<MutableList<Receipt>> {
        return receiptDao.selectAllReceipts()
                .compose(SchedulerUtils.ioToMain<MutableList<Receipt>>())
    }

    fun deleteAllReceipts(): Completable {
        return Completable.fromAction { receiptDao.deleteAllReceipts() }
                .compose(SchedulerUtils.ioToMain<Receipt>())
    }
}