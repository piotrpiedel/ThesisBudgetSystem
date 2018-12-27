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
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationCategoryTuple
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.util.simpleDateFormat
import javax.inject.Inject


class OperationAdapter @Inject constructor() : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {

    private var operationWithCategoryList: MutableList<OperationCategoryTuple> = mutableListOf()

    private var adapterListener: OperationAdapteListener? = null

    fun updateListOfOperations(operationListOther: List<OperationCategoryTuple>) {
        operationWithCategoryList.clear()
        operationWithCategoryList.addAll(operationListOther)
        notifyDataSetChanged()
    }

    fun setClickListener(operationAdapteListener: OperationAdapteListener) {
        adapterListener = operationAdapteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.operation_card_view, parent, false)
        return OperationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return operationWithCategoryList.size
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operationItem = operationWithCategoryList[position]
        holder.operation = operationItem.operation

        setCategoryTextView(holder, operationItem.category)

        setDateTextView(holder, operationItem.operation)

        holder.titleTextView.text = holder.operation?.title
        holder.valueTextView.text = holder.operation?.value.toString()
        if (operationItem.operation.operationType == OperationType.OUTCOME) {
            holder.valueTextView.setTextColor(Color.RED)
        } else {
            holder.valueTextView.setTextColor(Color.GREEN)
        }
    }

    private fun setCategoryTextView(holder: OperationViewHolder, operationItem: Category?) {
        holder.operation?.other_category_id?.let {
            holder.categoryTextView.visibility = View.VISIBLE
            holder.categoryTextView.text = operationItem?.category_title
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

    fun updateList(itemPosition: Int) {
        operationWithCategoryList.removeAt(itemPosition)
        notifyItemRemoved(itemPosition)
        notifyItemRangeChanged(itemPosition, itemCount)
    }


    interface OperationAdapteListener {
        fun onOperationListViewClicked(operation: Operation)
        fun onOperationsLongClick(operation: Operation, position: Int)
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
            itemView.setOnClickListener { adapterListener.let { adapterListener?.onOperationListViewClicked(operation as Operation) } }
            itemView.setOnLongClickListener {
                adapterListener.let { adapterListener?.onOperationsLongClick(operation as Operation, this.layoutPosition) }
                true
            }
        }

    }
}