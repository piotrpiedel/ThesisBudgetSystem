package piedel.piotr.thesis.ui.fragment.operation.operationlist

import android.annotation.SuppressLint
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class OperationPresenter @Inject
constructor(private val operationRepository: OperationRepository) : BasePresenter<OperationView>() {

    private var disposable: Disposable? = null

    fun addOperation() {
        view?.openAddOperationFragment()
    }

    @SuppressLint("CheckResult")
    fun loadOperations() {
        disposable = operationRepository.selectAllOperations()
                .compose(SchedulerUtils.ioToMain<List<Operation>>())
                .subscribe({ operations ->
                    view?.updateList(operations as MutableList<Operation>)
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                    view?.showError(throwable)
                })
        addDisposable(disposable)
    }

}