package piedel.piotr.thesis.ui.fragment.operation.operationlist

import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.ui.base.BaseView

interface OperationView : BaseView {

    fun openAddOperationFragment()

    fun updateList(operationsList: MutableList<Operation>)

    fun showError(throwable: Throwable)
}