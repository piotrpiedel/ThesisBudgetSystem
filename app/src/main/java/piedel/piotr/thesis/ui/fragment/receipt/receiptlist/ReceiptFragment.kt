package piedel.piotr.thesis.ui.fragment.receipt.receiptlist

import android.app.AlertDialog
import android.os.Bundle
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

class ReceiptFragment : BaseFragment(), ReceiptView, ReceiptAdapter.ReceiptAdapterListener {

    @Inject
    lateinit var receiptPresenter: ReceiptPresenter

    @Inject
    lateinit var receiptAdapter: ReceiptAdapter

    @BindView(R.id.recycler_view_receipt_list)
    lateinit var receiptListRecyclerView: RecyclerView

    override val layout: Int
        get() = R.layout.fragment_receipt_list

    override val toolbarTitle: String
        get() = FRAGMENT_TITLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        receiptPresenter.attachView(this)
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
                .setTitle(" Do you want to delete receipt? ")
                .setPositiveButton("YES") { _, _ ->
                    receiptPresenter.deleteActionReceipt(receiptItem, position)
                }
                .setNegativeButton("Cancel", null)
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
        Toast.makeText(context, "There is problem with operations, try again later", Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(show: Boolean) {
        getMainActivity().showProgressBar(show)
    }

    companion object {
        const val FRAGMENT_TAG: String = "ReceiptListFragment"
        const val FRAGMENT_TITLE: String = "Receipts"
    }
}