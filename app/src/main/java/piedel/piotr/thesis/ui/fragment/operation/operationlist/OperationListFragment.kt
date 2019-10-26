package piedel.piotr.thesis.ui.fragment.operation.operationlist

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_operations.*
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationCategoryTuple
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.operation.operationaddnew.OperationAddNewFragment
import piedel.piotr.thesis.util.showToast
import javax.inject.Inject

class OperationListFragment : BaseFragment(), OperationListContract.OperationView, OperationListAdapter.OperationAdapteListener {

    @Inject
    lateinit var operationListPresenter: OperationListPresenter

    @Inject
    lateinit var operationListAdapter: OperationListAdapter

    @BindView(R.id.recycler_view_operations)
    lateinit var operationsRecyclerView: RecyclerView

    @BindView(R.id.summary_value_text_view)
    lateinit var summaryValue: TextView

    override val toolbarTitle: String
        get() = context?.getString(R.string.operations_list).toString()

    override val layout: Int
        get() = R.layout.fragment_operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        operationListPresenter.attachView(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.fragment_operation_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_operations -> {
                operationListPresenter.deleteAllOperationsAction()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        operationListPresenter.initFragment()
    }

    override fun setAdapter() {
        operationsRecyclerView.adapter = operationListAdapter
        operationListAdapter.setClickListener(this)
    }

    override fun setOperationsRecyclerView() {
        operationsRecyclerView.layoutManager = LinearLayoutManager(context)

    }

    override fun openAddOperationFragment() {
        getBaseActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main, OperationAddNewFragment(), OperationAddNewFragment.FRAGMENT_TAG)
    }

    override fun updateSummary(summary: Double) {
        summaryValue.text = summary.toString()
        if (summary < 0) {
            summaryValue.setTextColor(resources.getColor(R.color.FireBrick, null))
            summary_text_view.setTextColor(resources.getColor(R.color.FireBrick, null))
        } else {
            summaryValue.setTextColor(Color.WHITE)
            summary_text_view.setTextColor(Color.WHITE)
        }
    }

    @OnClick(R.id.fragment_operation_button_add)
    fun addOperationButtonClicked() {
        operationListPresenter.addOperation()
    }

    override fun updateList(operationsList: List<OperationCategoryTuple>) {
        operationListAdapter.updateListOfOperations(operationsList)
    }

    override fun notifyItemRemoved(itemPosition: Int) {
        operationListAdapter.updateList(itemPosition)
    }

    override fun showError(throwable: Throwable) {
        showToast(requireContext(), getString(R.string.there_is_problem_with_operations_tr_again_later))
    }

    override fun onOperationListViewClicked(operation: Operation) {
        getBaseActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main, OperationAddNewFragment.newInstance(operation), OperationAddNewFragment.FRAGMENT_TAG)
    }

    override fun onOperationsLongClick(operation: Operation, position: Int) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.do_you_want_to_delete_operation))
                .setPositiveButton(getString(R.string.YES)) { _, _ ->
                    operationListPresenter.deleteActionOperation(operation, position)
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
        alertDialog.show()
    }

    companion object {
        const val FRAGMENT_TAG: String = "OperationListFragment"
    }
}