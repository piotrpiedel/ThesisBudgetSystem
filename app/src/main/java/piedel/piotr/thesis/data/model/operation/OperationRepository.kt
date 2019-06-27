package piedel.piotr.thesis.data.model.operation

import io.reactivex.Completable
import io.reactivex.Maybe
import piedel.piotr.thesis.util.fixNumberOfMonth
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import javax.inject.Inject

class OperationRepository @Inject constructor(private val operationDao: OperationDao) {

    fun insertOperation(operation: Operation): Completable {
        return Completable.fromAction { operationDao.insertOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
    }

    fun insertOperation(vararg operations: Operation): Completable {
        return Completable.fromAction { operationDao.insertOperation(*operations) }
                .compose(SchedulerUtils.ioToMain<Operation>())
    }

    fun updateOperation(operation: Operation): Completable {
        return Completable.fromAction { operationDao.updateOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
    }

    fun deleteOperation(operation: Operation): Completable {
        return Completable.fromAction { operationDao.deleteOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
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

    fun selectAllOperationsWithCategoriesOrderByDateDesc(): Maybe<List<OperationCategoryTuple>> {
        return operationDao.selectAllOperationsWithCategoriesOrderByDateDesc()
                .compose(SchedulerUtils.ioToMain<List<OperationCategoryTuple>>())
    }

    fun selectSumOfOperationByDate(): Maybe<List<DateValueTuple>> {
        return operationDao.selectSumOfOperationByDate()
                .compose(SchedulerUtils.ioToMain<List<DateValueTuple>>())
    }

    fun selectSumOfOperationByDateMonthly(monthOfOperations: Int, yearOfOperations: Int): Maybe<List<DateValueTuple>> {
        val monthFixedForDatabase = fixNumberOfMonth(monthOfOperations)
        return operationDao.selectSumOfOperationByDateMonthly(monthFixedForDatabase, yearOfOperations.toString())
                .compose(SchedulerUtils.ioToMain<List<DateValueTuple>>())
    }

    fun selectSummaryOperationByCategoryMonthly(monthOfOperations: Int, yearOfOperations: Int): Maybe<List<DateValueCategoryTuple>> {
        val monthFixedForDatabase = fixNumberOfMonth(monthOfOperations)
        return operationDao.selectSummaryOperationByCategoryMonthly(monthFixedForDatabase, yearOfOperations.toString())
                .compose(SchedulerUtils.ioToMain<List<DateValueCategoryTuple>>())
    }

    fun selectSummaryOperationByCategoryMonthlyOnlyOutcome(monthOfOperations: Int, yearOfOperations: Int): Maybe<List<DateValueCategoryTuple>> {
        val monthFixedForDatabase = fixNumberOfMonth(monthOfOperations)
        return operationDao.selectSummaryOperationByCategoryMonthlyOnlyOutcome(monthFixedForDatabase, yearOfOperations.toString())
                .compose(SchedulerUtils.ioToMain<List<DateValueCategoryTuple>>())
    }

    fun selectValueOperationList(): Maybe<List<OperationValueOperationType>> {
        return operationDao.selectValueOperationList()
                .compose(SchedulerUtils.ioToMain<List<OperationValueOperationType>>())
    }

    fun deleteAllOperations(): Completable {
        return Completable.fromAction { operationDao.deleteAllOperations() }
                .compose(SchedulerUtils.ioToMain<Operation>())
    }
}