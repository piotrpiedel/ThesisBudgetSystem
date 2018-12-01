package piedel.piotr.thesis.ui.fragment.operation.operationlist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.operation.operationaddview.AddOperationFragment
import javax.inject.Inject

class OperationFragment : BaseFragment(), OperationView, OperationAdapter.ClickListener {

    @Inject
    lateinit var operationPresenter: OperationPresenter

    @Inject
    lateinit var operationAdapter: OperationAdapter

    @BindView(R.id.recycler_view_operations)
    @JvmField
    var operationsRecyclerView: RecyclerView? = null


    override val layout: Int
        get() = R.layout.fragment_operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        operationPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOperationsRecyclerView()
        setAdapter()
        loadOperations()
    }

    private fun setAdapter() {
        operationsRecyclerView?.adapter = operationAdapter
        operationAdapter.setClickListener(this)
    }

    private fun setOperationsRecyclerView() {
        operationsRecyclerView?.layoutManager = LinearLayoutManager(context)

    }

    private fun loadOperations() {
        operationPresenter.loadOperations()
    }

    override fun openAddOperationFragment() {
        getBaseActivity().fragmentReplaceWithBackStack(R.id.fragment_container_activity_main, AddOperationFragment(), FRAGMENT_TAG)
    }

    @OnClick(R.id.fragment_operation_button_add)
    fun addOperationButtonClicked() {
        operationPresenter.addOperation()
    }

    override fun updateList(operationsList: MutableList<Operation>) {
        operationAdapter.setListOfOperations(operationsList)
        operationAdapter.notifyDataSetChanged()
    }

    override fun showError(throwable: Throwable) {
        Toast.makeText(context, "There is problem with operations, try again later", Toast.LENGTH_SHORT).show()
    }

    override fun onOperationsClick(operation: Operation) {
        getBaseActivity().fragmentReplaceWithBackStack(R.id.fragment_container_activity_main, AddOperationFragment.newInstance(operation), FRAGMENT_TAG)
    }

    companion object {
        const val FRAGMENT_TAG: String = "OperationFragment"
    }
}