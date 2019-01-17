package piedel.piotr.thesis.ui.fragment.category.categoryselectlist

import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.data.model.category.CategoryRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class CategorySelectListPresenter @Inject constructor(private val categoryRepository: CategoryRepository) : BasePresenter<CategorySelectListView>() {

    private var disposable: Disposable? = null

    var categoryExpandableGroupList: MutableList<CategoryExpandableGroup> = mutableListOf()

    fun loadParentCategories() {
        checkViewAttached()
        disposable =
                categoryRepository.selectParentCategories()
                        .compose(SchedulerUtils.ioToMain<List<Category>>())
                        .subscribe({ listCategories ->
                            loadChildForParentsCategories(listCategories)
                        }, { throwable ->
                            Timber.d(throwable.localizedMessage)
                        })
        addDisposable(disposable)
    }

    private fun loadChildForParentsCategories(listParentCategories: List<Category>) {
        disposable = categoryRepository.selectAllChildFromParents()
                .compose(SchedulerUtils.ioToMain<List<Category>>())
                .subscribe({ childCategoriesList ->
                    for (parent in listParentCategories) {
                        val listOfChild = childCategoriesList.filter { (it.parentCategoryId as Int) == parent.categoryId }
                        if (!listOfChild.isEmpty()) {
                            categoryExpandableGroupList.add(CategoryExpandableGroup(parent, listOfChild))
                        } else {
                            categoryExpandableGroupList.add(CategoryExpandableGroup(parent, emptyList()))
                        }
                    }
                    view?.updateList(categoryExpandableGroupList)
                }, {
                    Timber.d(it.localizedMessage)
                })
        addDisposable(disposable)
    }
}