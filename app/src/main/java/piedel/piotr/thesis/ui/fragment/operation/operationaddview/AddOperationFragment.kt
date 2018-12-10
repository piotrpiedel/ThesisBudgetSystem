package piedel.piotr.thesis.ui.fragment.operation.operationaddview

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.operation.operationlist.OperationFragment
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
    lateinit var textViewCategory: TextView

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

    override val toolbarTitle: String
        get() = FRAGMENT_TAG

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
        val calendar = Calendar.getInstance()
        val dateSetListener = onDateSetListener(calendar)
        setOnDateClickListener(dateSetListener, calendar)
    }

    private fun setOnDateClickListener(dateSetListener: DatePickerDialog.OnDateSetListener, cal: Calendar) {
        linearLayout.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    //            TODO: refactor this method
    private fun onDateSetListener(cal: Calendar): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { _, years, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, years)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            textViewDate.text = simpleDateFormat().format(cal.time)
        }
    }

    override fun fillTheData(operation: Operation?) {
        operation?.let {
            editTextInputValue.setText(operation.value.toString())
            textViewCategory.text = operation.other_category_id.toString()
            editTextTitle.setText(operation.title)
            textViewDate.text = stringFormatDate(operation.date)
            setRadioButtonChecked()
        }
    }

    private fun setRadioButtonChecked() {
        if (operation?.operationType == OperationType.OUTCOME) {
            radioButtonOutcomeOperation.isChecked = true
        } else {
            radioButtonIncomeOperation.isChecked = true
        }
    }

    @OnClick(R.id.operation_input_button_save)
    fun saveOperation() {
        val item: Operation = addOperationPresenter.createOperationToSave()
        addOperationPresenter.onSaveOperationButtonClicked(item)
    }

    override fun createOperationToSave(): Operation {
        val valueOfOperation = radioButtonChecked()
        return addOperationPresenter.prepareOperationToSave(operation,
                editTextInputValue.text.toString(),
                editTextTitle.text.toString(),
                valueOfOperation,
                textViewDate.text.toString(),
                null)
    }

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

        fun newInstance(operation: Operation): AddOperationFragment {
            val addOperationFragment = AddOperationFragment()
            val args = Bundle()
            args.putParcelable(OPERATION_KEY, operation)
            addOperationFragment.arguments = args
            return addOperationFragment
        }
    }

}
