package piedel.piotr.thesis.ui.fragment.category.categorylist

import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.fragment.category.categoryselectlist.CategoryExpandableGroup

interface CategoryView : BaseView {

    fun updateList(listCategories: MutableList<CategoryExpandableGroup>)

}