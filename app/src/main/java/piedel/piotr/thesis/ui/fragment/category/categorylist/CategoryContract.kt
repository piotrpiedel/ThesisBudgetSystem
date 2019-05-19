package piedel.piotr.thesis.ui.fragment.category.categorylist

import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter
import piedel.piotr.thesis.ui.fragment.category.categoryselectlist.CategoryExpandableGroup

interface CategoryContract {

    interface CategoryView : BaseView {
        fun updateList(listCategories: MutableList<CategoryExpandableGroup>)
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun loadCategories();
    }
}