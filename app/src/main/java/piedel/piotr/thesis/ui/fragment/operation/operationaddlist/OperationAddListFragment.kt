package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationSelectable
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.showToast
import javax.inject.Inject

class OperationAddListFragment : BaseFragment(), OperationAddListContract.OperationView {

    @Inject
    lateinit var operationAddListPresenter: OperationAddListPresenter

    @Inject
    lateinit var operationAddListAdapter: OperationAddListAdapter

    @BindView(R.id.recycler_view_operations)
    lateinit var operationsRecyclerView: RecyclerView

    private var operationArrayList: ArrayList<OperationSelectable> = arrayListOf()

    override val toolbarTitle: String
        get() = context?.getString(R.string.operations_list).toString()

    override val layout: Int
        get() = R.layout.fragment_list_add_operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        operationAddListPresenter.attachView(this)
        setMenuEnabled()
    }

    private fun setMenuEnabled() {
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        operationArrayList = arguments?.getParcelableArrayList(OPERATION_LIST_PASSED)
                ?: arrayListOf() // temp
        operationArrayList.add(OperationSelectable(Operation("asfasf")))
        operationArrayList.add(OperationSelectable(Operation("asfas2eqw")))
        operationArrayList.add(OperationSelectable(Operation("asfas232")))
        operationArrayList.add(OperationSelectable(Operation("asfasfasfas232")))
        operationAddListPresenter.initFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_operation_add_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_action -> {
                operationAddListPresenter.saveSelectedOperations(operationArrayList)
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
        operationsRecyclerView.adapter = operationAddListAdapter
        operationAddListAdapter.operationWithCategoryList = operationArrayList.toMutableList()
        operationAddListAdapter.notifyDataSetChanged()
    }

    override fun showError(throwable: Throwable) {
        showToast(requireContext(), getString(R.string.there_is_problem_with_operations_tr_again_later))
    }

    companion object {
        const val FRAGMENT_TAG: String = "OperationAddListFragment"
        const val OPERATION_LIST_PASSED: String = "OPERATION_KEY"

        fun newInstance(operationList: ArrayList<OperationSelectable>): OperationAddListFragment {
            val operationAddListFragment = OperationAddListFragment()
            val args = Bundle().apply {
                putParcelableArrayList(OPERATION_LIST_PASSED, operationList)
            }
            operationAddListFragment.arguments = args
            return operationAddListFragment
        }
    }
}
