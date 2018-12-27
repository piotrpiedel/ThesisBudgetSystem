package piedel.piotr.thesis.ui.fragment.operation.operationaddview

import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.data.model.category.CategoryRepository
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.dateFromString
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ConfigPersistent
class AddOperationPresenter @Inject
constructor(private val operationsRepository: OperationRepository, private val categoryRepository: CategoryRepository) : BasePresenter<AddOperationView>() {

    private var disposable: Disposable? = null

    fun onSaveOperationButtonClicked(operation: Operation) {
        checkIfOperationExist(operation)
    }

    private fun checkIfOperationExist(operation: Operation) {
        disposable = operationsRepository.selectOperation(operation.id)
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(
                        {
                            Timber.d("OnSucces: checkIfOperationExist onSucces update and return ")
                            updateOperation(operation)
                            view?.returnFromFragment()
                        },
                        { t ->
                            Timber.d("OnError: checkIfOperationExist %s", t.localizedMessage)
                        },
                        {
                            Timber.d("OnComplete: insertion from checkIfOperation exist ")
                            insertOperation(operation)
                        }
                )
        addDisposable(disposable)
    }


    private fun insertOperation(operation: Operation) {
        Completable.fromAction { operationsRepository.insertOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        view?.returnFromFragment()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }
                })
    }

    private fun updateOperation(operation: Operation) {
        Completable.fromAction { operationsRepository.updateOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Timber.d("updateOperation onComplete")
                        view?.returnFromFragment()
                    }

                    override fun onSubscribe(d: Disposable) {
                        Timber.d("updateOperation onSubscribe")
                    }

                    override fun onError(e: Throwable) {
                        Timber.d("updateOperation onError")
                        Timber.d(e)
                    }
                })
    }

    fun prepareOperationToSave(operationOther: Operation?, inputValue: String, textTitle: String, valueOfOperation: OperationType, dateOther: String, category: Category?): Operation {
        val valueOfOperationOther = inputValue.toDouble()
        val dateFromString = dateFromString(dateOther)
        return operationOther?.let(updateExistingOperation(valueOfOperationOther, textTitle, valueOfOperation, dateFromString, category))
                ?: run {
                    Operation(valueOfOperationOther,
                            textTitle,
                            valueOfOperation,
                            dateFromString,
                            category?.categoryId)
                }
    }

    private fun updateExistingOperation(valueOfOperationOther: Double, textTitle: String, valueOfOperation: OperationType, dateFromString: Date?, category: Category?): (Operation) -> Operation {
        return { operation ->
            operation.apply {
                value = valueOfOperationOther
                title = textTitle
                operationType = valueOfOperation
                date = dateFromString
                other_category_id = category?.categoryId
            }
        }
    }

    fun fillTheData(operation: Operation?) {
        operation?.other_category_id?.let {
            loadOperationWithCategory(operation, it)
        } ?: run {
            operation?.let {
                loadOperation(operation)
            } ?: return
        }
    }

    fun createOperationToSave(): Operation {
        return view?.createOperationToSave() as Operation
    }

    private fun loadOperationWithCategory(operation: Operation, operationId: Int) {
        disposable = categoryRepository.selectCategory(operationId)
                .compose(SchedulerUtils.ioToMain<List<Category>>())
                .subscribe({
                    val categoryForOperation = it.first()
                    view?.fillTheData(operation, categoryForOperation)
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                })
        addDisposable(disposable)
    }

    private fun loadOperation(operation: Operation) {
        view?.fillTheData(operation, null)
    }
}