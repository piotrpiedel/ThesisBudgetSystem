package piedel.piotr.thesis.ui.fragment.operation.operationlist

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationCategoryTuple
import piedel.piotr.thesis.data.model.operation.OperationRepository
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
        loadOperationsWithCategories()
        loadSummary()
    }

    private fun loadSummary() {
        disposable = operationRepository.selectValueOperationList()
                .compose(SchedulerUtils.ioToMain<List<OperationValueOperationType>>())
                .subscribe({
                    var summary = 0.0
                    for (item in it) {
                        summary += item.value
                    }
                    val truncatedSummary = BigDecimal.valueOf(summary).setScale(3, RoundingMode.FLOOR).toDouble()
                    view?.updateSummary(truncatedSummary)
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
    fun loadOperationsWithCategories() {
        disposable = operationRepository.selectAllOperationsWithCategories()
                .compose(SchedulerUtils.ioToMain<List<OperationCategoryTuple>>())
                .subscribe({ operations ->
                    view?.updateList(operations)
                    loadSummary()
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

    fun deleteActionOperation(operation: Operation, itemPosition: Int) {
        Completable.fromAction { operationRepository.deleteOperation(operation) }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(object : CompletableObserverMain() {
                    override fun onComplete() {
                        Timber.d("deleteActionOperation on Complete")
                        notifyAdapterItemRemoved(itemPosition)
                    }
                })
    }

    private fun notifyAdapterItemRemoved(itemPosition: Int) {
        view?.notifyItemRemoved(itemPosition)
        loadSummary()
    }

    fun deleteAllOperationsAction() {
        Completable.fromAction { operationRepository.deleteAllOperations() }
                .compose(SchedulerUtils.ioToMain<Operation>())
                .subscribe(object : CompletableObserverMain() {
                    override fun onComplete() {
                        val operations = emptyList<OperationCategoryTuple>()
                        view?.updateList(operations)
                    }
                })
    }


}