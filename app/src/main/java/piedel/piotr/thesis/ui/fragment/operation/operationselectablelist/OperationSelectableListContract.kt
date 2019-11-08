package piedel.piotr.thesis.ui.fragment.operation.operationselectablelist

import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface OperationSelectableListContract {
    interface OperationView : BaseView {

        fun showError(throwable: Throwable)

        fun setOperationsRecyclerView()

        fun setAdapter()

        fun showInsertCompleteToast()
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initFragment(operationArrayList: ArrayList<Operation>)
    }
}