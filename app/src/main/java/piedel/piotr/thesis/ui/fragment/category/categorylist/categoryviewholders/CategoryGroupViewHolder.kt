package piedel.piotr.thesis.ui.fragment.category.categorylist.categoryviewholders

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.github.pavlospt.roundedletterview.RoundedLetterView
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import piedel.piotr.thesis.R
import piedel.piotr.thesis.util.getRandomColor


class CategoryGroupViewHolder(itemView: View) : GroupViewHolder(itemView) {
    private val parentCategoryGroupTitle = itemView.findViewById(R.id.categories_list_expandable_group_header) as TextView
    private val parentCategoryGroupTitleLetterView = itemView.findViewById(R.id.round_letter_view) as RoundedLetterView

    fun onBindParent(parentCategoryTitle: String) {
        parentCategoryGroupTitle.setTypeface(null, Typeface.BOLD)
        parentCategoryGroupTitle.text = parentCategoryTitle
        parentCategoryGroupTitleLetterView.titleText = parentCategoryTitle[0].toString()
        parentCategoryGroupTitleLetterView.backgroundColor = getRandomColor()
    }

}