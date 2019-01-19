package piedel.piotr.thesis.ui.fragment.operation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.pavlospt.roundedletterview.RoundedLetterView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.util.getRandomColor

class SpinnerCategoryAdapter(context: Context, categoryChildChildList: MutableList<CategoryChild>) : ArrayAdapter<CategoryChild>(context, 0, categoryChildChildList) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.operation_input_category_row, parent, false)
        }
        val roundedLetterView: RoundedLetterView = view?.findViewById(R.id.round_letter_view) as RoundedLetterView
        val headerOfSpinner: TextView = view.findViewById(R.id.categories_list_expandable_group_header) as TextView

        val currentItem: CategoryChild = getItem(position) as CategoryChild

        currentItem.let {
            headerOfSpinner.text = currentItem.category_title
            roundedLetterView.titleText = currentItem.category_title[0].toString()
            roundedLetterView.backgroundColor = getRandomColor()
        }
        return view
    }
}
