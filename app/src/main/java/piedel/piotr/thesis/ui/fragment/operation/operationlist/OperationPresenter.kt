package piedel.piotr.thesis.ui.fragment.operation.operationlist

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.data.model.operation.OperationValueOperationType
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject


@ConfigPersistent
class OperationPresenter @Inject
constructor(private val operationRepository: OperationRepository) : BasePresenter<OperationView>() {

    private var disposable: Disposable? = null


    fun initFragment() {
        checkViewAttached()
        view?.setOperationsRecyclerView()
        view?.setAdapter()
        loadOperations()
        loadSummary()
    }

    private fun loadSummary() {
        disposable = operationRepository.selectValueOperationList()
                .compose(SchedulerUtils.ioToMain<List<OperationValueOperationType>>())
                .subscribe({
                    var summary = 0.0
                    for (item in it) {
                        if (item.operationType == OperationType.INCOME) {
                            summary += item.value
                        } else if (item.operationType == OperationType.OUTCOME) {
                            summary -= item.value
                        }
                    }
                    val truncatedSummary = BigDecimal.valueOf(summary)
                            .setScale(3, RoundingMode.HALF_UP)
                            .toDouble()
                    view?.updateSummary(truncatedSummary)
                    Timber.d(it.toString())
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                    view?.showError(throwable)
                })
        addDisposable(disposable)
    }

    fun addOperation() {
        view?.openAddOperationFragment()
    }

    @SuppressLint("CheckResult")
    fun loadOperations() {
        disposable = operationRepository.selectAllOperations()
                .compose(SchedulerUtils.ioToMain<List<Operation>>())
                .subscribe({ operations ->
                    Timber.d("loadOperations()")
                    view?.updateList(operations as MutableList<Operation>)
                    loadSummary()
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

    fun deleteActionOperation(operation: Operation) {
        Completable.fromAction { operationRepository.deleteOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(object : CompletableObserverMain() {
                    override fun onComplete() {
                        Timber.d("deleteActionOperation on Complete")
                        loadOperations()
                    }
                })
    }
}