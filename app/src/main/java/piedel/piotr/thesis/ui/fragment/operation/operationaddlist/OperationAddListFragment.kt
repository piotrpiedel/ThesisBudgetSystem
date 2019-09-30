package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.showToast
import timber.log.Timber
import javax.inject.Inject

class OperationAddListFragment : BaseFragment(), OperationAddListContract.OperationView, OperationAddListAdapter.OperationAdapterListener {

    @Inject
    lateinit var operationAddListPresenter: OperationAddListPresenter

    @Inject
    lateinit var operationAddListAdapter: OperationAddListAdapter

    @BindView(R.id.recycler_view_operations)
    lateinit var operationsRecyclerView: RecyclerView

    private var operationArrayList: ArrayList<Operation> = arrayListOf()

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
        operationArrayList.add(Operation("asfasf"))
        operationArrayList.add(Operation("asffas"))
        operationArrayList.add(Operation("asfas2eqw"))
        operationArrayList.add(Operation("asfas232"))
        operationArrayList.add(Operation("asfasfasfas232"))
        operationAddListPresenter.initFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.fragment_operation_add_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save_action -> {
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
//        operationAddListAdapter.operationWithCategoryList = operationArrayList.toMutableList()
        operationAddListAdapter.notifyDataSetChanged()
        Timber.d("setAdapter()")
//        operationAddListAdapter.updateListOfOperations(operationArrayList.toList())
//        operationAddListAdapter.setClickListener(this)
    }

    override fun onOperationItemClicked() {

    }

    override fun showError(throwable: Throwable) {
        showToast(requireContext(), getString(R.string.there_is_problem_with_operations_tr_again_later))
    }

    companion object {
        const val FRAGMENT_TAG: String = "OperationAddListFragment"
        const val OPERATION_LIST_PASSED: String = "OPERATION_KEY"

        fun newInstance(operationList: ArrayList<Operation>): OperationAddListFragment {
            val operationAddListFragment = OperationAddListFragment()
            val args = Bundle().apply {
                putParcelableArrayList(OPERATION_LIST_PASSED, operationList)
            }
            operationAddListFragment.arguments = args
            return operationAddListFragment
        }
    }
}
