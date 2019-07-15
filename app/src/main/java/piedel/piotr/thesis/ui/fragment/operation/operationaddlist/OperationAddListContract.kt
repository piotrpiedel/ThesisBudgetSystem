package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface OperationAddListContract {
    interface OperationView : BaseView {

        fun showError(throwable: Throwable)

        fun setOperationsRecyclerView()

        fun setAdapter()

    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initFragment()
    }
}