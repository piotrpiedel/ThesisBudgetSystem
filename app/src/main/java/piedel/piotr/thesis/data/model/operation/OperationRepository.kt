package piedel.piotr.thesis.data.model.operation

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Maybe
import piedel.piotr.thesis.util.fixNumberOfMonth
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import timber.log.Timber
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

    fun selectAllOperations(): Maybe<List<Operation>> {
        return operationDao.selectAllOperations()
                .compose(SchedulerUtils.ioToMain<List<Operation>>())
    }

    fun selectAllOperationsWithCategories(): Maybe<List<OperationCategoryTuple>> {
        return operationDao.selectAllOperationsWithCategories()
                .compose(SchedulerUtils.ioToMain<List<OperationCategoryTuple>>())
    }

    fun selectSumOfOperationByDate(): Maybe<List<DateValueTuple>> {
        return operationDao.selectSumOfOperationByDate()
                .compose(SchedulerUtils.ioToMain<List<DateValueTuple>>())
    }

    fun selectSumOfOperationByDateMonthly(monthOfOperations: Int, yearOfOperations: Int): Maybe<List<DateValueTuple>> {
        val monthFixedForDatabase = fixNumberOfMonth(monthOfOperations)
        Timber.d("selectSumOfOperationByDateMonthly month: %s,  year: %s", monthFixedForDatabase, yearOfOperations.toString())
        return operationDao.selectSumOfOperationByDateMonthly(monthFixedForDatabase, yearOfOperations.toString())
                .compose(SchedulerUtils.ioToMain<List<DateValueTuple>>())
    }

    fun selectSummaryOperationByCategoryMonthly(monthOfOperations: Int, yearOfOperations: Int): Maybe<List<DateValueCategoryTuple>> {
        val monthFixedForDatabase = fixNumberOfMonth(monthOfOperations)
        Timber.d("selectSumOfOperationByDateMonthly month: %s,  year: %s", monthFixedForDatabase, yearOfOperations.toString())
        return operationDao.selectSummaryOperationByCategoryMonthly(monthFixedForDatabase, yearOfOperations.toString())
                .compose(SchedulerUtils.ioToMain<List<DateValueCategoryTuple>>())
    }

    fun selectValueOperationList(): Maybe<List<OperationValueOperationType>> {
        return operationDao.selectValueOperationList()
                .compose(SchedulerUtils.ioToMain<List<OperationValueOperationType>>())
    }

    fun deleteAllOperations() {
        Completable.fromAction { operationDao.deleteAllOperations() }
                .compose(SchedulerUtils.ioToMain<Operation>())
    }
}