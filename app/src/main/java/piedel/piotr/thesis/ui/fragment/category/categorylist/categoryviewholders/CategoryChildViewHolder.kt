package piedel.piotr.thesis.ui.fragment.category.categorylist.categoryviewholders

import android.view.View
import android.widget.TextView
import com.github.pavlospt.roundedletterview.RoundedLetterView
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.util.getRandomColor


open class CategoryChildViewHolder(itemView: View) : ChildViewHolder(itemView) {

    var childCategoryChild: CategoryChild? = null

    private val childCategoryTitle = itemView.findViewById(R.id.categories_list_expandable_item_title) as TextView
    private val childCategoryTitleLetterView = itemView.findViewById(R.id.round_letter_view) as RoundedLetterView

    fun onBindChild(childCategoryChild: CategoryChild) {
        childCategoryTitle.text = childCategoryChild.category_title
        childCategoryTitleLetterView.titleText = childCategoryChild.category_title[0].toString()
        childCategoryTitleLetterView.backgroundColor = getRandomColor()
    }
}