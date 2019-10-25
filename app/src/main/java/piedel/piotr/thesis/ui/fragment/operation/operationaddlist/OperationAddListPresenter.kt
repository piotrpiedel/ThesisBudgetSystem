package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.data.model.operation.OperationSelectable
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.operation.operationaddlist.OperationAddListContract.OperationView
import piedel.piotr.thesis.ui.fragment.operation.operationaddlist.OperationAddListContract.PresenterContract
import timber.log.Timber
import javax.inject.Inject


@ConfigPersistent
class OperationAddListPresenter @Inject
constructor(private val operationRepository: OperationRepository) : BasePresenter<OperationView>(), PresenterContract<OperationView> {

    private var disposable: Disposable? = null

    override fun initFragment() {
        checkViewAttached()
        view?.setOperationsRecyclerView()
        view?.setAdapter()
    }

    fun saveSelectedOperations(operationList: ArrayList<OperationSelectable>) {
        val selectedOperationList: Array<Operation> = operationList
                .filter { operationSelectable -> operationSelectable.selected }
                .map { operationSelectable -> operationSelectable.operation }
                .toTypedArray()

        operationRepository.insertOperation(*selectedOperationList)
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Timber.d("oncomplete fucker")
                    }

                    override fun onSubscribe(d: Disposable) {
                        Timber.d("onSubscribe fucker")
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e, "onError")
                    }

                })
    }
}