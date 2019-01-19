package piedel.piotr.thesis.data.model.category.categorychild

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Maybe
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import javax.inject.Inject

@SuppressLint("CheckResult")
class CategoryChildRepository @Inject constructor(val categoryChildDao: CategoryChildDao) {


    fun insertCategory(categoryChild: CategoryChild) {
        Completable.fromAction { categoryChildDao.insertCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
                .subscribe(CompletableObserverMain())
    }

    fun insertAllCategory(categoryChildren: Array<CategoryChild>) {
        Completable.fromAction { categoryChildDao.insertAllCategory(*categoryChildren) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
                .subscribe(CompletableObserverMain())
    }

    fun updateCategory(categoryChild: CategoryChild) {
        Completable.fromAction { categoryChildDao.updateCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
                .subscribe(CompletableObserverMain())
    }

    fun deleteCategory(categoryChild: CategoryChild) {
        Completable.fromAction { categoryChildDao.deleteCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
                .subscribe(CompletableObserverMain())
    }

    fun selectCategory(idCategoryOther: Int): Maybe<List<CategoryChild>> {
        return categoryChildDao.selectCategory(idCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

    fun selectChildCategory(idCategoryOther: Int, parentCategoryOther: Int): Maybe<List<CategoryChild>> {
        return categoryChildDao.selectChildCategory(idCategoryOther, parentCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

    fun selectAllChildFromParentCategory(parentCategoryOther: Int): Maybe<List<CategoryChild>> {
        return categoryChildDao.selectAllChildFromParentCategory(parentCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

    fun selectAllCategories(): Maybe<List<CategoryChild>> {
        return categoryChildDao.selectAllCategories()
    }

    fun selectAllChildFromParents(): Maybe<List<CategoryChild>> {
        return categoryChildDao.selectAllChildFromParents()
    }

    fun selectParentCategories(): Maybe<List<CategoryChild>> {
        return categoryChildDao.selectParentCategories()
    }

    fun deleteAllCategories() {
        Completable.fromAction { categoryChildDao.deleteAllCategories() }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
    }


}