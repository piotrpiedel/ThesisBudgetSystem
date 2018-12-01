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
import piedel.piotr.thesis.util.simpleDateFormat
import piedel.piotr.thesis.util.stringFormatDate
import java.util.*
import javax.inject.Inject


class AddOperationFragment : BaseFragment(), AddOperationView {

    @Inject
    lateinit var addOperationPresenter: AddOperationPresenter

    @BindView(R.id.operation_input_value)
    @JvmField
    var editTextInputValue: EditText? = null

    @BindView(R.id.operation_input_category)
    @JvmField
    var textViewCategory: TextView? = null

    @BindView(R.id.operation_input_title)
    @JvmField
    var editTextTitle: EditText? = null

    @BindView(R.id.operation_input_income)
    @JvmField
    var radioButtonIncomeOperation: RadioButton? = null

    @BindView(R.id.operation_input_outcome)
    @JvmField
    var radioButtonOutcomeOperation: RadioButton? = null

    @BindView(R.id.operation_input_date)
    @JvmField
    var textViewDate: TextView? = null

    @BindView(R.id.calendar_container)
    @JvmField
    var linearLayout: LinearLayout? = null

    private var operation: Operation? = null

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
        linearLayout?.setOnClickListener {
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
            textViewDate?.text = simpleDateFormat().format(cal.time)
        }
    }

    override fun fillTheData(operation: Operation?) {
        operation?.let {
            editTextInputValue?.setText(operation.value.toString())
            textViewCategory?.text = operation.other_category_id.toString()
            editTextTitle?.setText(operation.title)
            textViewDate?.text = stringFormatDate(operation.date)
            setRadioButtonChecked()
        }
    }

    private fun setRadioButtonChecked() {
        if (operation?.operationType == OperationType.OUTCOME) {
            radioButtonOutcomeOperation?.isChecked = true
        } else {
            radioButtonIncomeOperation?.isChecked = true
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
                editTextInputValue?.text.toString(),
                editTextTitle?.text.toString(),
                valueOfOperation,
                textViewDate?.text.toString(),
                null)
    }

    private fun radioButtonChecked(): OperationType {
        return if (radioButtonIncomeOperation?.isChecked == true) {
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

        fun newInstance(operation: Operation): AddOperationFragment {
            val addOperationFragment = AddOperationFragment()
            val args = Bundle()
            args.putParcelable(OPERATION_KEY, operation)
            addOperationFragment.arguments = args
            return addOperationFragment
        }
    }

}
