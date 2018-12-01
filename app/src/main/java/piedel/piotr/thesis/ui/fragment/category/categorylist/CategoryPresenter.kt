package piedel.piotr.thesis.ui.fragment.category.categorylist

import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.data.model.category.CategoryRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class CategoryPresenter @Inject constructor(private val categoryRepository: CategoryRepository) : BasePresenter<CategoryView>() {

    private var categoryListHeader: MutableList<Category> = mutableListOf()

    private var listChildHashMap: MutableMap<Int, MutableList<Category>> = mutableMapOf()

    fun loadParentCategories() {
        checkViewAttached()
        disposable =
                categoryRepository.selectParentCategories()
                        .compose(SchedulerUtils.ioToMain<List<Category>>())
                        //TODO: i should do this with flatMap, swichMap, oraz just map
//        https://stackoverflow.com/questions/26935821/rxjava-chaining-observables
                        .subscribe({ listCategories ->

                            loadChildForParentsCategories(listCategories)
                            categoryListHeader = listCategories as MutableList<Category>
//                            Timber.d("loadCategories" + listChildHashMap.toString())

                            view?.updateList(categoryListHeader, listChildHashMap)
                        }, { throwable ->
                            Timber.d(throwable.localizedMessage)
                        })
        addDisposable(disposable)
    }

    private fun loadChildForParentsCategories(listParentCategories: List<Category>) {
        disposable = categoryRepository.selectAllChildFromParents()
                .compose(SchedulerUtils.ioToMain<List<Category>>())
                .subscribe({ childCategoriesList ->
                    //                    Timber.d("loadChildForParentsCategories" + childCategoriesList.toString())
                    for (parent in listParentCategories) {
                        val listOfChild = childCategoriesList.filter { (it.parentCategoryId as Int) == parent.categoryId }
//                        Timber.d("loop for listOfChild: " + listOfChild.toString())
                        if (!listOfChild.isEmpty()) {
//                            Timber.d("inside if " + listOfChild.toString())
                            parent.categoryId?.let { listChildHashMap.put(parent.categoryId, listOfChild as MutableList<Category>) }
                        }
                    }
                    view?.updateList(categoryListHeader, listChildHashMap)
                }, {
                    Timber.d(it.localizedMessage)
                })
        addDisposable(disposable)
    }

    private var disposable: Disposable? = null

}