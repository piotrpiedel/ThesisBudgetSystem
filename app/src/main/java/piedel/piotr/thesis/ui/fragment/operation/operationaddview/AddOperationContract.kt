package piedel.piotr.thesis.ui.fragment.operation.operationaddview

import android.widget.EditText
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface AddOperationContract {
    interface AddOperationView : BaseView {

        fun returnFromFragment()

        fun fillTheData(operation: Operation?, categoryChild: CategoryChild?)

        fun enableSaveButton(isEnabled: Boolean)

        fun setRadioButtonChecked()

    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun onSaveOperationButtonClicked(operation: Operation?, inputValue: String, textTitle: String, valueOfOperation: OperationType, dateOther: String, operationCategoryChild: CategoryChild?)

        fun fillTheData(operation: Operation?)

        fun setRadioButtonChecked()

        fun observeTheInputValue(editTextInputValue: EditText)


    }
}