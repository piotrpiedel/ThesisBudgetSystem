package piedel.piotr.thesis.ui.fragment.category.categorylist

import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.ui.base.BaseView

interface CategoryView : BaseView {

    fun updateList(listCategories: List<Category>, listChildHashMap: MutableMap<Int, MutableList<Category>>)

}