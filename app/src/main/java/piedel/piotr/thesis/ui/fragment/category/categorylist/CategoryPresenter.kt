package piedel.piotr.thesis.ui.fragment.category.categorylist

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import piedel.piotr.thesis.data.model.category.CategoryRepository
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.data.model.category.categoryparent.CategoryParent
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.fragment.category.categoryselectlist.CategoryExpandableGroup
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class CategoryPresenter @Inject constructor(private val categoryRepository: CategoryRepository) : BasePresenter<CategoryContract.CategoryView>(), CategoryContract.PresenterContract<CategoryContract.CategoryView> {

    private var disposable: Disposable? = null

    override fun loadCategories() {
        checkViewAttached()
        disposable =
                categoryRepository.selectParentCategories() // get all parent Categories
                        // flatMapSingle -  flatMap but on the end need to emit only once (Single)
                        .flatMapSingle { categories ->
                            // create observable from list parentsCategoriesList into single items
                            Observable.fromIterable(categories)
                                    // flat Map of single item
                                    .flatMap { categoryParent ->
                                        Observable.just(categoryParent).zipWith(categoryRepository.selectAllChildFromParentCategory(categoryParent.category_id_parent), //select Observable<CategoryChild>
                                                object : BiFunction<CategoryParent, List<CategoryChild>, Pair<CategoryParent, List<CategoryChild>>> { // biFunction take two arguments and return third
                                                    override fun apply(outCategoryParent: CategoryParent, outListCategoryChild: List<CategoryChild>): Pair<CategoryParent, List<CategoryChild>> {
                                                        return Pair(outCategoryParent, outListCategoryChild)
                                                    }
                                                })
                                    }
                                    .toList() // the end - need to emit only once (Single) eg. list, collect to List
                        }
                        .subscribe({ list ->
                            val categoryExpandableGroupList: MutableList<CategoryExpandableGroup> = mutableListOf()
                            for (i in 0 until list.size) {
                                val pairFromList = list[i]
                                if (!pairFromList.second.isEmpty()) { // uncategorized has no subcategories
                                    categoryExpandableGroupList.add(CategoryExpandableGroup(pairFromList.first.category_title_parent, pairFromList.second))
                                } else {
                                    categoryExpandableGroupList.add(CategoryExpandableGroup(pairFromList.first.category_title_parent, emptyList()))
                                }
                            }
                            view?.updateList(categoryExpandableGroupList)
                        },
                                { Timber.d("categoryPresenter loadCategories exception:  %s", it.toString()) },
                                { Timber.d("categoryPresenter loadCategories onComplete") }
                        )
        addDisposable(disposable)
    }
}