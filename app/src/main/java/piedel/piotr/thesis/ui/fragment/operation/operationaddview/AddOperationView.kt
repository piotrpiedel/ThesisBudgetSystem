package piedel.piotr.thesis.ui.fragment.operation.operationaddview

import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.ui.base.BaseView

interface AddOperationView : BaseView {

    fun returnFromFragment()

    fun fillTheData(operation: Operation?)

    fun createOperationToSave(): Operation
}