package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationSelectable
import piedel.piotr.thesis.util.dateToDayMonthYearFormatString
import piedel.piotr.thesis.util.doubleToStringInTwoPlacesAfterComma
import javax.inject.Inject


class OperationAddListAdapter @Inject constructor() : RecyclerView.Adapter<OperationAddListAdapter.OperationViewHolder>() {

    var operationWithCategoryList: MutableList<OperationSelectable> = mutableListOf()

    private lateinit var adapterAddListListener: OperationAdapterListener

    init {
        setHasStableIds(true)
    }

    fun updateListOfOperations(operationListOther: List<OperationSelectable>?) {
        operationWithCategoryList.clear()
        operationListOther?.let { operationWithCategoryList.addAll(it) }
        notifyDataSetChanged()
    }

    fun setClickListener(operationAdapterListener: OperationAdapterListener) {
        adapterAddListListener = operationAdapterListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.operation_card_view_selectable, parent, false)
        return OperationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return operationWithCategoryList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operationItem = operationWithCategoryList[position]
        holder.bind(operationItem)
    }

    interface OperationAdapterListener {
        fun onOperationItemClicked()
    }

    inner class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(operationItem: OperationSelectable) {
            operationItem.selected = false
            setCategoryTextView(this, operationItem)
            setDateTextView(this, operationItem)
            titleTextView.text = operationItem.title
            valueTextView.text = doubleToStringInTwoPlacesAfterComma(operationItem.value)
        }

        private fun setCategoryTextView(holder: OperationViewHolder, operationItem: OperationSelectable?) {
            operationItem?.other_category_id?.let {
                holder.categoryTextView.visibility = View.VISIBLE
                holder.categoryTextView.text = operationItem.other_category_id.toString()
            } ?: run {
                holder.categoryTextView.visibility = View.GONE
            }
        }

        private fun setDateTextView(holder: OperationViewHolder, operationItem: OperationSelectable?) {
            operationItem?.date?.let {
                holder.dateTextView.visibility = View.VISIBLE
                holder.dateTextView.text = operationItem.date?.let { date -> dateToDayMonthYearFormatString(date) }
            } ?: run {
                holder.dateTextView.visibility = View.GONE
            }
        }

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
//            itemView.setOnClickListener { adapterAddListListener.onOperationItemClicked() }
        }
    }
}