package piedel.piotr.thesis.ui.fragment.operation.operationselectablelist

import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.data.model.operation.OperationSelectable
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.operation.operationselectablelist.OperationSelectableListContract.OperationView
import piedel.piotr.thesis.ui.fragment.operation.operationselectablelist.OperationSelectableListContract.PresenterContract
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import javax.inject.Inject


@ConfigPersistent
class OperationSelectableListPresenter @Inject
constructor(private val operationRepository: OperationRepository) : BasePresenter<OperationView>(), PresenterContract<OperationView> {

    private var disposable: Disposable? = null
    var operationArrayList: MutableList<OperationSelectable> = mutableListOf()

    override fun initFragment(operationArrayList: ArrayList<Operation>) {
        this.operationArrayList = operationArrayList.map { OperationSelectable(it, false) }.toMutableList()
        checkViewAttached()
        view?.setOperationsRecyclerView()
        view?.setAdapter()
    }

    fun saveSelectedOperations(operationList: MutableList<OperationSelectable>) {
        val selectedOperationList: Array<Operation> = operationList
                .filter { operationSelectable -> operationSelectable.selected }
                .map { operationSelectable -> operationSelectable.operation }
                .toTypedArray()

        operationRepository.insertOperation(*selectedOperationList)
                .subscribe(object : CompletableObserverMain() {})
    }
}