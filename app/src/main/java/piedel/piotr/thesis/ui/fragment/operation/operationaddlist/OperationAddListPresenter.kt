package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.operation.operationaddlist.OperationAddListContract.OperationView
import piedel.piotr.thesis.ui.fragment.operation.operationaddlist.OperationAddListContract.PresenterContract
import java.util.ArrayList
import javax.inject.Inject


@ConfigPersistent
class OperationAddListPresenter @Inject
constructor(private val operationRepository: OperationRepository) : BasePresenter<OperationView>(), PresenterContract<OperationView> {

    private var disposable: Disposable? = null

    override fun initFragment(operationArrayList: ArrayList<Operation>?) {
        checkViewAttached()
        view?.setOperationsRecyclerView()
//        view?.setAdapter()
    }

}