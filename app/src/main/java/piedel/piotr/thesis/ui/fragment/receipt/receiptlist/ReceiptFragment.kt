package piedel.piotr.thesis.ui.fragment.receipt.receiptlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptadd.ReceiptAddFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptpreview.ReceiptPreviewFragment
import javax.inject.Inject

class ReceiptFragment : BaseFragment(), ReceiptContract.ReceiptView, ReceiptAdapter.ReceiptAdapterListener {

    @Inject
    lateinit var receiptPresenter: ReceiptPresenter

    @Inject
    lateinit var receiptAdapter: ReceiptAdapter

    @BindView(R.id.recycler_view_receipt_list)
    lateinit var receiptListRecyclerView: RecyclerView

    override val layout: Int
        get() = R.layout.fragment_receipt_list

    override val toolbarTitle: String
        get() = context?.getString(R.string.receipts).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        receiptPresenter.attachView(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.fragment_receipt_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete_all_receipts -> {
                receiptPresenter.deleteAllReceiptsAction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiptPresenter.initFragment()
    }

    @OnClick(R.id.fragment_receipt_button_add)
    fun onAddNewReceiptButtonClicked() {
        receiptPresenter.onAddNewReceiptClicked()
    }

    override fun replaceWithAddReceiptFragment() {
        getBaseActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main, ReceiptAddFragment(), ReceiptAddFragment.FRAGMENT_TAG)
    }

    override fun setReceiptListRecyclerView() {
        receiptListRecyclerView.layoutManager = LinearLayoutManager(context)

    }

    override fun setAdapter() {
        receiptListRecyclerView.adapter = receiptAdapter
        receiptAdapter.setClickListener(this)
    }

    override fun updateList(receipts: MutableList<Receipt>) {
        receiptAdapter.updateListOfReceipt(receipts)
    }

    override fun onReceiptLongClick(receiptItem: Receipt, position: Int) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.do_you_want_to_delete_receipt))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    receiptPresenter.deleteActionReceipt(receiptItem, position)
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
        alertDialog.show()
    }

    override fun notifyItemRemoved(itemPosition: Int) {
        receiptAdapter.notifyAboutItemRemoved(itemPosition)
    }

    override fun onClickListener(receiptItem: Receipt) {
        getBaseActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main, ReceiptPreviewFragment.newInstance(receiptItem), ReceiptPreviewFragment.FRAGMENT_TAG)
    }

    override fun showError(throwable: Throwable?) {
        Toast.makeText(context, getString(R.string.there_is_problem_with_receipts_try_again_later), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(show: Boolean) {
        getMainActivity().showProgressBar(show)
    }

    companion object {
        const val FRAGMENT_TAG: String = "ReceiptListFragment"
    }
}