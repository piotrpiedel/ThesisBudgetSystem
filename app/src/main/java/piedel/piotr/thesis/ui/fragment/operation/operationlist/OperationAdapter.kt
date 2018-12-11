package piedel.piotr.thesis.ui.fragment.operation.operationlist

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.util.simpleDateFormat
import timber.log.Timber
import javax.inject.Inject


class OperationAdapter @Inject constructor() : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {

    private var operationsList: MutableList<Operation>? = null

    private var onClickListener: ClickListener? = null

    init {
        operationsList = mutableListOf<Operation>()
    }

    fun updateListOfOperations(operationListOther: MutableList<Operation>) {
        operationsList?.clear()
        operationsList?.addAll(operationListOther)
        Timber.d("notifyDataSetChanged()")
        Timber.d(operationsList.toString())
        notifyDataSetChanged()
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

        holder.titleTextView.text = holder.operation?.title
        holder.valueTextView.text = holder.operation?.value.toString()
        if (operationItem?.operationType == OperationType.OUTCOME) {
            holder.valueTextView.setTextColor(Color.RED)
        } else {
            holder.valueTextView.setTextColor(Color.GREEN)
        }
    }

    private fun setCategoryTextView(holder: OperationViewHolder, operationItem: Operation?) {
        holder.operation?.other_category_id?.let {
            holder.categoryTextView.visibility = View.VISIBLE
            holder.categoryTextView.text = operationItem?.other_category_id.toString()
        } ?: run {
            holder.categoryTextView.visibility = View.GONE
        }
    }

    private fun setDateTextView(holder: OperationViewHolder, operationItem: Operation?) {
        holder.operation?.date?.let {
            holder.dateTextView.visibility = View.VISIBLE
            holder.dateTextView.text = simpleDateFormat().format(operationItem?.date)
        } ?: run {
            holder.dateTextView.visibility = View.GONE
        }
    }


    interface ClickListener {
        fun onOperationsClick(operation: Operation)
        fun onOperationsLongClick(operation: Operation)
    }

    inner class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var operation: Operation? = null

        @BindView(R.id.operations_title)
        lateinit var titleTextView: TextView

        @BindView(R.id.operations_category)
        lateinit var categoryTextView: TextView

        @BindView(R.id.operations_value)
        lateinit var valueTextView: TextView

        @BindView(R.id.operations_date)
        lateinit var dateTextView: TextView

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener { onClickListener.let { onClickListener?.onOperationsClick(operation as Operation) } }
            itemView.setOnLongClickListener {
                onClickListener.let { onClickListener?.onOperationsLongClick(operation as Operation) }
                true
            }
        }

    }
}