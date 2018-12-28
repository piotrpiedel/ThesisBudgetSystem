package piedel.piotr.thesis.ui.fragment.operation.operationaddview

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.category.categoryselectlist.CategorySelectListFragment
import piedel.piotr.thesis.ui.fragment.category.view.CategorySelectionLayout
import piedel.piotr.thesis.util.addOperationFragmentRequestCode
import piedel.piotr.thesis.util.simpleDateFormat
import piedel.piotr.thesis.util.stringFormatDate
import java.util.*
import javax.inject.Inject


class AddOperationFragment : BaseFragment(), AddOperationView {

    @Inject
    lateinit var addOperationPresenter: AddOperationPresenter

    @BindView(R.id.operation_input_value)
    lateinit var editTextInputValue: EditText

    @BindView(R.id.operation_input_category)
    lateinit var categorySelection: CategorySelectionLayout

    @BindView(R.id.operation_input_title)
    lateinit var editTextTitle: EditText

    @BindView(R.id.operation_input_income)
    lateinit var radioButtonIncomeOperation: RadioButton

    @BindView(R.id.operation_input_outcome)
    lateinit var radioButtonOutcomeOperation: RadioButton

    @BindView(R.id.operation_input_date)
    lateinit var textViewDate: TextView

    @BindView(R.id.calendar_container)
    lateinit var linearLayout: LinearLayout

    private var operation: Operation? = null

    var operationCategory: Category? = null

    override val toolbarTitle: String
        get() = FRAGMENT_TITLE

    override val layout: Int
        get() = R.layout.fragment_operations_add

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        addOperationPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        operation = arguments?.getParcelable(OPERATION_KEY)
        addOperationPresenter.fillTheData(operation)
        setDatePickerDialog()
        setOnCategoryListener()
        setCategory()
    }

    private fun setCategory() {
        operationCategory?.let {
            setCategorySelectionView(operationCategory as Category)
        }
    }

    private fun setOnCategoryListener() {
        categorySelection.setOnClickListener {
            val categorySelectListFragment = CategorySelectListFragment()
            categorySelectListFragment.setTargetFragment(this, addOperationFragmentRequestCode)
            getBaseActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                    categorySelectListFragment,
                    CategorySelectListFragment.FRAGMENT_TAG)
        }
    }

    private fun setDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val dateSetListener = onDateSetListener(calendar)
        setOnDateClickListener(dateSetListener, calendar)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == addOperationFragmentRequestCode && resultCode == Activity.RESULT_OK) {
            operationCategory = data?.getParcelableExtra(CategorySelectListFragment.FRAGMENT_INTENT_CATEGORY)
        }
    }

    private fun setCategorySelectionView(category: Category) {
        categorySelection.setView(category)
    }

    //TODO: refactor : Do some Util out of this class
    private fun setOnDateClickListener(dateSetListener: DatePickerDialog.OnDateSetListener, cal: Calendar) {
        linearLayout.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                    cal.get(Calendar.YEAR), //the initially selected year
                    cal.get(Calendar.MONTH), //the initially selected month
                    cal.get(Calendar.DAY_OF_MONTH)) //the initially selected day of month
                    .show()
        }
    }

    //TODO: refactor : Do some Util out of this class
    private fun onDateSetListener(cal: Calendar): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { _, years, monthOfYear, dayOfMonth ->
            setSelectedDate(cal, years, monthOfYear, dayOfMonth)
            textViewDate.text = simpleDateFormat().format(cal.time)
        }
    }

    private fun setSelectedDate(cal: Calendar, years: Int, monthOfYear: Int, dayOfMonth: Int) {
        cal.set(Calendar.YEAR, years)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    override fun fillTheData(operation: Operation?, category: Category?) {
        if (operationCategory == null) {
            operationCategory = category
        }
        operation?.let {
            editTextInputValue.setText(operation.value.toString())
            operationCategory?.let { it1 -> categorySelection.setView(it1) }
            editTextTitle.setText(operation.title)
            textViewDate.text = stringFormatDate(operation.date)
            setRadioButtonChecked()
        }

    }

    //TODO: refactor : Do some Util out of this class or presenter?
    private fun setRadioButtonChecked() {
        if (operation?.operationType == OperationType.OUTCOME) {
            radioButtonOutcomeOperation.isChecked = true
        } else {
            radioButtonIncomeOperation.isChecked = true
        }
    }

    @OnClick(R.id.operation_input_button_save)
    fun saveOperation() {
        addOperationPresenter.onSaveOperationButtonClicked(addOperationPresenter.createOperationToSave())
    }

    override fun createOperationToSave(): Operation {
        return addOperationPresenter.prepareOperationToSave(
                operation,
                editTextInputValue.text.toString(),
                editTextTitle.text.toString(),
                radioButtonChecked(),
                textViewDate.text.toString(),
                operationCategory)
    }

    //TODO: find some other way?
    private fun radioButtonChecked(): OperationType {
        return if (radioButtonIncomeOperation.isChecked) {
            OperationType.INCOME
        } else {
            OperationType.OUTCOME
        }
    }

    override fun returnFromFragment() {
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    companion object {

        const val OPERATION_KEY: String = "OPERATION_KEY"

        const val FRAGMENT_TAG: String = "Add Operation Fragment"
        const val FRAGMENT_TITLE: String = " Add Operation "

        fun newInstance(operation: Operation): AddOperationFragment {
            val addOperationFragment = AddOperationFragment()
            val args = Bundle()
            args.putParcelable(OPERATION_KEY, operation)
            addOperationFragment.arguments = args
            return addOperationFragment
        }
    }

}
