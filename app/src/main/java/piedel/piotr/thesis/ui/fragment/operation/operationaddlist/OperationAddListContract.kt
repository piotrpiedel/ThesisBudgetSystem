package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationCategoryTuple
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface OperationAddListContract {
    interface OperationView : BaseView {

        fun openAddOperationFragment()

        fun updateList(operationsList: List<OperationCategoryTuple>)

        fun notifyItemRemoved(itemPosition: Int)

        fun showError(throwable: Throwable)

        fun setOperationsRecyclerView()

        fun setAdapter()

        fun updateSummary(summary: Double)

    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initFragment()

        fun addOperation()

        fun loadOperationsWithCategories()

        fun deleteActionOperation(operation: Operation, itemPosition: Int)

        fun deleteAllOperationsAction()
    }
}