package piedel.piotr.thesis.ui.fragment.category.categoryselectlist

import piedel.piotr.thesis.ui.base.BaseView

interface CategorySelectListView : BaseView {

    fun updateList(listCategories: MutableList<CategoryExpandableGroup>)

}
