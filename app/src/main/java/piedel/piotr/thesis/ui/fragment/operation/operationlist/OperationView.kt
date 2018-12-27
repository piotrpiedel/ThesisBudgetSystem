package piedel.piotr.thesis.ui.fragment.operation.operationlist

import piedel.piotr.thesis.data.model.operation.OperationCategoryTuple
import piedel.piotr.thesis.ui.base.BaseView

interface OperationView : BaseView {

    fun openAddOperationFragment()

    fun updateList(operationsList: List<OperationCategoryTuple>)

    fun notifyItemRemoved(itemPosition: Int)

    fun showError(throwable: Throwable)

    fun setOperationsRecyclerView()

    fun setAdapter()

    fun updateSummary(summary: Double)

}