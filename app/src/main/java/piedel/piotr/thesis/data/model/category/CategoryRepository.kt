package piedel.piotr.thesis.data.model.category

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChildDao
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChildParentTuple
import piedel.piotr.thesis.data.model.category.categoryparent.CategoryParent
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import javax.inject.Inject

@SuppressLint("CheckResult")
class CategoryRepository @Inject constructor(val categoryChildDao: CategoryChildDao) {


    fun insertCategory(categoryChild: CategoryChild): Completable {
        return Completable.fromAction { categoryChildDao.insertCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
    }

    fun insertAllCategory(categoryChildren: Array<CategoryChild>): Completable {
        return Completable.fromAction { categoryChildDao.insertAllCategory(*categoryChildren) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
    }

    fun updateCategory(categoryChild: CategoryChild): Completable {
        return Completable.fromAction { categoryChildDao.updateCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
    }

    fun deleteCategory(categoryChild: CategoryChild): Completable {
        return Completable.fromAction { categoryChildDao.deleteCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
    }

    fun selectCategory(idCategoryOther: Int): Maybe<List<CategoryChild>> {
         return categoryChildDao.selectCategory(idCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

    fun selectChildCategory(idCategoryOther: Int, parentCategoryOther: Int): Maybe<List<CategoryChild>> {
        return categoryChildDao.selectChildCategory(idCategoryOther, parentCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

    fun selectAllChildFromParentCategory(parentCategoryOther: Int): Observable<List<CategoryChild>> {
        return categoryChildDao.selectAllChildFromParentCategory(parentCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

    fun selectChildCategories(): Flowable<List<CategoryChild>> {
        return categoryChildDao.selectChildCategories()
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

    fun selectAllChildFromParents(): Observable<List<CategoryChild>> {
        return categoryChildDao.selectAllChildFromParents()
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

    fun selectParentCategories(): Observable<List<CategoryParent>> {
        return categoryChildDao.selectParentCategories()
                .compose(SchedulerUtils.ioToMain<List<CategoryParent>>())
    }

    fun selectCategoriesDividedByParents(): Maybe<List<CategoryChildParentTuple>> {
        return categoryChildDao.selectCategoriesDividedByParents()
                .compose(SchedulerUtils.ioToMain<List<CategoryChildParentTuple>>())
    }

    fun deleteAllCategories(): Completable {
        return Completable.fromAction { categoryChildDao.deleteAllCategories() }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
    }


}