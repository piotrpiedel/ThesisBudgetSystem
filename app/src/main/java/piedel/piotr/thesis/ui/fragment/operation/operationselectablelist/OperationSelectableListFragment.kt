package piedel.piotr.thesis.ui.fragment.operation.operationselectablelist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.showToast
import javax.inject.Inject

class OperationSelectableListFragment : BaseFragment(), OperationSelectableListContract.OperationView {

    @Inject
    lateinit var operationSelectableListPresenter: OperationSelectableListPresenter

    @Inject
    lateinit var operationSelectableListAdapter: OperationSelectableListAdapter

    @BindView(R.id.recycler_view_operations)
    lateinit var operationsRecyclerView: RecyclerView


    override val toolbarTitle: String
        get() = context?.getString(R.string.operations_list).toString()

    override val layout: Int
        get() = R.layout.fragment_list_add_operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        operationSelectableListPresenter.attachView(this)
        setMenuEnabled()
    }

    private fun setMenuEnabled() {
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        operationSelectableListPresenter.initFragment(arguments?.getParcelableArrayList(OPERATION_LIST_PASSED) ?: arrayListOf())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_operation_add_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_action -> {
                operationSelectableListPresenter.saveSelectedOperations(operationSelectableListPresenter.operationArrayList)
                true
            }
            R.id.cancel_action -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setOperationsRecyclerView() {
        operationsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun setAdapter() {
        operationsRecyclerView.adapter = operationSelectableListAdapter
        operationSelectableListAdapter.listOfOperationSelectable = operationSelectableListPresenter.operationArrayList
        operationSelectableListAdapter.notifyDataSetChanged()
    }

    override fun showError(throwable: Throwable) {
        showToast(requireContext(), getString(R.string.there_is_problem_with_operations_tr_again_later))
    }

    override fun showInsertCompleteToast() {
        Toast.makeText(context, getString(R.string.loading_operation_from_html_success), Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val FRAGMENT_TAG: String = "OperationSelectableListFragment"
        const val OPERATION_LIST_PASSED: String = "OPERATION_KEY"

        fun newInstance(operationList: List<Operation>): OperationSelectableListFragment {
            val operationAddListFragment = OperationSelectableListFragment()
            val args = Bundle().apply {
                putParcelableArrayList(OPERATION_LIST_PASSED, ArrayList(operationList))
            }
            operationAddListFragment.arguments = args
            return operationAddListFragment
        }
    }
}
