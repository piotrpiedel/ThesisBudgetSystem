package piedel.piotr.thesis.ui.fragment.operation.operationaddview

import android.annotation.SuppressLint
import android.widget.EditText
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChildRepository
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.dateFrom_DAY_MONTH_YEAR_TO_YEAR_MONT_DAY
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ConfigPersistent
class AddOperationPresenter @Inject
constructor(private val operationsRepository: OperationRepository, private val categoryChildRepository: CategoryChildRepository) : BasePresenter<AddOperationView>() {

    private var disposable: Disposable? = null

    fun onSaveOperationButtonClicked(operation: Operation?, inputValue: String, textTitle: String, valueOfOperation: OperationType, dateOther: String, operationCategoryChild: CategoryChild?) {
        updateOrInsertOperation(returnPreparedNewOperationOrUpdated(operation, inputValue, textTitle, valueOfOperation, dateOther, operationCategoryChild))
    }

    private fun updateOrInsertOperation(operation: Operation) {
        disposable = operationsRepository.selectOperation(operation.id)
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(
                        {
                            Timber.d("OnSucces: updateOrInsertOperation onSucces update and return ")
                            updateOperation(operation)
                            view?.returnFromFragment()
                        },
                        { t ->
                            Timber.d("OnError: updateOrInsertOperation %s", t.localizedMessage)
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

    fun returnPreparedNewOperationOrUpdated(operation: Operation?, inputValue: String,
                                            textTitle: String, valueOfOperation: OperationType,
                                            dateOther: String, operationCategoryChild: CategoryChild?): Operation {
        return prepareNewOrUpdateOperation(operation, inputValue, textTitle,
                valueOfOperation, dateOther, operationCategoryChild)

    }

    fun prepareNewOrUpdateOperation(operationOther: Operation?, inputValue: String,
                                    textTitle: String, valueOfOperation: OperationType,
                                    dateOtherString: String, categoryChild: CategoryChild?): Operation {

        var valueOfOperationOther = inputValue.toDouble()
        if (valueOfOperation == OperationType.OUTCOME) {
            valueOfOperationOther *= -1
        }
        val dateFromString = dateFrom_DAY_MONTH_YEAR_TO_YEAR_MONT_DAY(dateOtherString)
        Timber.d("date %s", dateFromString?.toString())
        return operationOther
                ?.let(updateExistingOperation(valueOfOperationOther, textTitle, valueOfOperation, dateFromString, categoryChild))
                ?: run {
                    Operation(valueOfOperationOther,
                            textTitle,
                            valueOfOperation,
                            dateFromString,
                            categoryChild?.categoryId)
                }
    }

    private fun updateExistingOperation(valueOfOperationOther: Double, textTitle: String,
                                        valueOfOperation: OperationType, dateFromString: Date?,
                                        categoryChild: CategoryChild?): (Operation) -> Operation {
        return { operation ->
            operation.apply {
                value = valueOfOperationOther
                title = textTitle
                operationType = valueOfOperation
                date = dateFromString
                other_category_id = categoryChild?.categoryId
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


    private fun loadOperationWithCategory(operation: Operation, operationId: Int) {
        disposable = categoryChildRepository.selectCategory(operationId)
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
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

    @SuppressLint("CheckResult")
    fun observeTheInputValue(editTextInputValue: EditText) {
        editTextInputValue.textChanges()
                .skip(1)
                .debounce(700, TimeUnit.MILLISECONDS)
                .compose(SchedulerUtils.ioToMain())
                .subscribe({
                    if (it.isNotBlank())
                        view?.enableSaveButton(true)
                    else view?.enableSaveButton(false)
                }, {
                    Timber.d("observeTheInputValue $it.localizedMessage")
                })
    }
}