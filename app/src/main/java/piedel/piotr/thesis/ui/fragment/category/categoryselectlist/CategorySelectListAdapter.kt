package piedel.piotr.thesis.ui.fragment.category.categoryselectlist

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.pavlospt.roundedletterview.RoundedLetterView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.util.getRandomColor
import timber.log.Timber


class CategorySelectListAdapter constructor(var group: MutableList<CategoryExpandableGroup>) : ExpandableRecyclerViewAdapter<CategorySelectListAdapter.CategoryGroupViewHolder, CategorySelectListAdapter.CategoryChildViewHolder>(group) {

    lateinit var categorySelectListAdapterOnClickHandler: CategorySelectListAdapterOnClickHandler

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CategoryGroupViewHolder? {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.categories_list_expandable_group, parent, false)
        return CategoryGroupViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): CategoryChildViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.categories_list_expandable_item, parent, false)
        return CategoryChildViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: CategoryChildViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val childCategoryChild: CategoryChild = group?.items?.get(childIndex) as CategoryChild
        holder?.childCategoryChild = childCategoryChild
        holder?.onBindChild(childCategoryChild)
    }

    override fun onBindGroupViewHolder(holder: CategoryGroupViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        val parentCategoryTitle = group?.title
        parentCategoryTitle?.let { holder?.onBindParent(it) }
    }

    fun setOnChildClickListener(categorySelectListAdapterOnClickHandler: CategorySelectListAdapterOnClickHandler) {
        this.categorySelectListAdapterOnClickHandler = categorySelectListAdapterOnClickHandler
    }

    interface CategorySelectListAdapterOnClickHandler {
        fun onChildClick(childCategoryChild: CategoryChild)
    }

    fun updateList(passedList: MutableList<CategoryExpandableGroup>) {
        this.group.clear()
        this.group.addAll(passedList)
        notifyDataSetChanged()
    }

    inner class CategoryGroupViewHolder(itemView: View) : GroupViewHolder(itemView) {

        private val parentCategoryGroupTitle = itemView.findViewById(R.id.categories_list_expandable_group_header) as TextView
        private val parentCategoryGroupTitleLetterView = itemView.findViewById(R.id.round_letter_view) as RoundedLetterView

        fun onBindParent(parentCategoryTitle: String) {
            parentCategoryGroupTitle.setTypeface(null, Typeface.BOLD)
            parentCategoryGroupTitle.text = parentCategoryTitle
            parentCategoryGroupTitleLetterView.titleText = parentCategoryTitle[0].toString()
            parentCategoryGroupTitleLetterView.backgroundColor = getRandomColor()
        }

    }

    inner class CategoryChildViewHolder(itemView: View) : ChildViewHolder(itemView) {

        var childCategoryChild: CategoryChild? = null

        private val childCategoryTitle = itemView.findViewById(R.id.categories_list_expandable_item_title) as TextView
        private val childCategoryTitleLetterView = itemView.findViewById(R.id.round_letter_view) as RoundedLetterView

        init {
            itemView.setOnClickListener { childCategoryChild?.let { it1 -> categorySelectListAdapterOnClickHandler.onChildClick(it1) } }
        }

        fun onBindChild(childCategoryChild: CategoryChild) {
            childCategoryTitle.text = childCategoryChild.category_title
            childCategoryTitleLetterView.titleText = childCategoryChild.category_title[0].toString()
            childCategoryTitleLetterView.backgroundColor = getRandomColor()
        }
    }
}

class CategoryExpandableGroup(category_title_parent: String, items: List<CategoryChild>) : ExpandableGroup<CategoryChild>(category_title_parent, items)