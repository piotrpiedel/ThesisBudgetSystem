package piedel.piotr.thesis.ui.fragment.operation.operationlist

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationCategoryTuple
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.operation.operationaddview.AddOperationFragment
import javax.inject.Inject

class OperationFragment : BaseFragment(), OperationView, OperationAdapter.OperationAdapteListener {


    @Inject
    lateinit var operationPresenter: OperationPresenter

    @Inject
    lateinit var operationAdapter: OperationAdapter

    @BindView(R.id.recycler_view_operations)
    lateinit var operationsRecyclerView: RecyclerView

    @BindView(R.id.summary_value_text_view)
    lateinit var summaryValue: TextView

    override val toolbarTitle: String
        get() = FRAGMENT_TITLE

    override val layout: Int
        get() = R.layout.fragment_operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        operationPresenter.attachView(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        operationPresenter.initFragment()
    }

    override fun setAdapter() {
        operationsRecyclerView.adapter = operationAdapter
        operationAdapter.setClickListener(this)
    }

    override fun setOperationsRecyclerView() {
        operationsRecyclerView.layoutManager = LinearLayoutManager(context)

    }

    override fun openAddOperationFragment() {
        getBaseActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main, AddOperationFragment(), AddOperationFragment.FRAGMENT_TAG)
    }

    override fun updateSummary(summary: Double) {
        summaryValue.text = summary.toString()
    }

    @OnClick(R.id.fragment_operation_button_add)
    fun addOperationButtonClicked() {
        operationPresenter.addOperation()
    }

    override fun updateList(operationsList: List<OperationCategoryTuple>) {
        operationAdapter.updateListOfOperations(operationsList)
    }

    override fun notifyItemRemoved(itemPosition: Int) {
        operationAdapter.updateList(itemPosition)
    }

    override fun showError(throwable: Throwable) {
        Toast.makeText(context, "There is problem with operations, try again later", Toast.LENGTH_SHORT).show()
    }

    override fun onOperationListViewClicked(operation: Operation) {
        getBaseActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main, AddOperationFragment.newInstance(operation), AddOperationFragment.FRAGMENT_TAG)
    }

    override fun onOperationsLongClick(operation: Operation, position: Int) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle(" Do you want to delete operation? ")
                .setPositiveButton("YES") { _, _ ->
                    operationPresenter.deleteActionOperation(operation, position)
                }
                .setNegativeButton("Cancel", null)
                .create()
        alertDialog.show()
    }

    companion object {
        const val FRAGMENT_TAG: String = "OperationFragment"
        const val FRAGMENT_TITLE: String = "Operations"
    }
}