package piedel.piotr.thesis.ui.fragment.operation.operationselectablelist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.OperationSelectable
import piedel.piotr.thesis.util.dateToDayMonthYearFormatString
import piedel.piotr.thesis.util.doubleToStringInTwoPlacesAfterComma
import javax.inject.Inject

class OperationSelectableListAdapter @Inject constructor() : RecyclerView.Adapter<OperationSelectableListAdapter.OperationViewHolder>() {

    var listOfOperationSelectable: MutableList<OperationSelectable> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.operation_card_view_selectable, parent, false)
        return OperationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfOperationSelectable.size
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        holder.bind(listOfOperationSelectable[position], holder)
    }

    inner class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.operations_title)
        lateinit var titleTextView: TextView

        @BindView(R.id.operations_category)
        lateinit var categoryTextView: TextView

        @BindView(R.id.operations_value)
        lateinit var valueTextView: TextView

        @BindView(R.id.operations_date)
        lateinit var dateTextView: TextView

        private lateinit var selectableOperationItem: OperationSelectable

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener {
                selectableOperationItem.selected = !selectableOperationItem.isSelected()
                titleTextView.setBackgroundColor((if (selectableOperationItem.selected) Color.CYAN else Color.WHITE))
            }
        }

        fun bind(operationItem: OperationSelectable, holder: OperationViewHolder) {
            selectableOperationItem = operationItem
            holder.itemView.setBackgroundColor(if (selectableOperationItem.isSelected()) Color.CYAN else Color.WHITE)
            setCategoryTextView(this, operationItem)
            setDateTextView(this, operationItem)
            titleTextView.text = operationItem.title
            valueTextView.text = doubleToStringInTwoPlacesAfterComma(operationItem.value)
        }

        private fun setCategoryTextView(holder: OperationViewHolder, operationItem: OperationSelectable?) {
            if (operationItem?.other_category_id != null) {
                holder.categoryTextView.visibility = View.VISIBLE
                holder.categoryTextView.text = operationItem.other_category_id.toString()
            } else {
                holder.categoryTextView.visibility = View.GONE
            }
        }

        private fun setDateTextView(holder: OperationViewHolder, operationItem: OperationSelectable?) {
            if (operationItem?.date != null) {
                holder.dateTextView.visibility = View.VISIBLE
                holder.dateTextView.text = operationItem.date?.let { date -> dateToDayMonthYearFormatString(date) }
            } else {
                holder.dateTextView.visibility = View.GONE
            }
        }
    }

}