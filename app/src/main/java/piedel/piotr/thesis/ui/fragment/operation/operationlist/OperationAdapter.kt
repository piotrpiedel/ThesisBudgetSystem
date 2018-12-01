package piedel.piotr.thesis.ui.fragment.operation.operationlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.injection.scopes.ActivityContext
import piedel.piotr.thesis.util.simpleDateFormat
import javax.inject.Inject


//TODO: change and remove @ActivityContext val context: Context but first check if this fucks works
class OperationAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {

    private var operationsList: MutableList<Operation>? = null

    private var onClickListener: ClickListener? = null

    init {
        operationsList = mutableListOf<Operation>()
    }

    fun setListOfOperations(operationListOther: MutableList<Operation>) {
        operationsList?.clear()
        operationsList?.addAll(operationListOther)
        operationsList = operationListOther
    }

    fun setClickListener(clickListener: ClickListener) {
        onClickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.operation_card_view, parent, false)
        return OperationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return operationsList?.size as Int
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operationItem = operationsList?.get(position)
        holder.operation = operationItem

        setCategoryTextView(holder, operationItem)

        setDateTextView(holder, operationItem)

        holder.titleTextView?.text = holder.operation?.title
        holder.valueTextView?.text = holder.operation?.value.toString()
    }

    private fun setCategoryTextView(holder: OperationViewHolder, operationItem: Operation?) {
        holder.operation?.other_category_id?.let {
            holder.categoryTextView?.visibility = View.VISIBLE
            holder.categoryTextView?.text = operationItem?.other_category_id.toString()
        } ?: run {
            holder.categoryTextView?.visibility = View.GONE
        }
    }

    private fun setDateTextView(holder: OperationViewHolder, operationItem: Operation?) {
        holder.operation?.date?.let {
            holder.dateTextView?.visibility = View.VISIBLE
            holder.dateTextView?.text = simpleDateFormat().format(operationItem?.date)
        } ?: run {
            holder.dateTextView?.visibility = View.GONE
        }
    }


    interface ClickListener {
        fun onOperationsClick(operation: Operation) // TODO: edytować operację - update operacji + usuniecie operacji? onLongClickListener?
    }

    inner class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var operation: Operation? = null

        @BindView(R.id.operations_title)
        @JvmField
        var titleTextView: TextView? = null

        @BindView(R.id.operations_category)
        @JvmField
        var categoryTextView: TextView? = null

        @BindView(R.id.operations_value)
        @JvmField
        var valueTextView: TextView? = null

        @BindView(R.id.operations_date)
        @JvmField
        var dateTextView: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener { if (onClickListener != null) onClickListener?.onOperationsClick(operation as Operation) }
        }

    }
}