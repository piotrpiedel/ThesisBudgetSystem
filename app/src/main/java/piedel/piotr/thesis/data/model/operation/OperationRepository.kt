package piedel.piotr.thesis.data.model.operation

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Maybe
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import javax.inject.Inject

@SuppressLint("CheckResult")

class OperationRepository @Inject constructor(private val operationDao: OperationDao) {

    fun insertOperation(operation: Operation) {
        Completable.fromAction { operationDao.insertOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(CompletableObserverMain())
    }

    fun insertOperation(vararg operations: Operation) {
        Completable.fromAction { operationDao.insertOperation(*operations) }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(CompletableObserverMain())
    }

    fun updateOperation(operation: Operation) {
        Completable.fromAction { operationDao.updateOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(CompletableObserverMain())
    }

    fun deleteOperation(operation: Operation) {
        Completable.fromAction { operationDao.deleteOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(CompletableObserverMain())
    }

    fun selectOperation(idOperationOther: Int): Maybe<Operation> {
        return operationDao.selectOperation(idOperationOther)
                .compose(SchedulerUtils.ioToMain<Operation>())
    }

    fun selectOperationsFromCategory(idOperationOther: Int): Maybe<List<Operation>> {
        return operationDao.selectAllOperations()
                .compose(SchedulerUtils.ioToMain<List<Operation>>())
    }

    fun selectAllOperations(): Maybe<List<Operation>> {
        return operationDao.selectAllOperations()
                .compose(SchedulerUtils.ioToMain<List<Operation>>())

    }

    fun deleteAllOperations() {
        Completable.fromAction { operationDao.deleteAllOperations() }
                .compose(SchedulerUtils.ioToMain<Operation>())
    }
}