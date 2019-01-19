package piedel.piotr.thesis.data.model.category.categoryparent

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Maybe
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import javax.inject.Inject

@SuppressLint("CheckResult")
class CategoryParentRepository @Inject constructor(val categorParentDao: CategoryParentDao) {


    fun insertCategory(categoryChild: CategoryChild) {
        Completable.fromAction { categorParentDao.insertCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
                .subscribe(CompletableObserverMain())
    }

    fun insertAllCategory(categoryChildren: Array<CategoryChild>) {
        Completable.fromAction { categorParentDao.insertAllCategory(*categoryChildren) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
                .subscribe(CompletableObserverMain())
    }

    fun updateCategory(categoryChild: CategoryChild) {
        Completable.fromAction { categorParentDao.updateCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
                .subscribe(CompletableObserverMain())
    }

    fun deleteCategory(categoryChild: CategoryChild) {
        Completable.fromAction { categorParentDao.deleteCategory(categoryChild) }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
                .subscribe(CompletableObserverMain())
    }

    fun selectCategory(idCategoryOther: Int): Maybe<List<CategoryChild>> {
        return categorParentDao.selectCategory(idCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
    }

//    fun selectChildCategory(idCategoryOther: Int, parentCategoryOther: Int): Maybe<List<CategoryChild>> {
//        return categorParentDao.selectChildCategory(idCategoryOther, parentCategoryOther)
//                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
//    }
//
//    fun selectAllChildFromParentCategory(parentCategoryOther: Int): Maybe<List<CategoryChild>> {
//        return categorParentDao.selectAllChildFromParentCategory(parentCategoryOther)
//                .compose(SchedulerUtils.ioToMain<List<CategoryChild>>())
//    }

    fun selectAllCategories(): Maybe<List<CategoryChild>> {
        return categorParentDao.selectAllCategories()
    }

//    fun selectAllChildFromParents(): Maybe<List<CategoryChild>> {
//        return categorParentDao.selectAllChildFromParents()
//    }
//
//    fun selectParentCategories(): Maybe<List<CategoryChild>> {
//        return categorParentDao.selectParentCategories()
//    }

    fun deleteAllCategories() {
        Completable.fromAction { categorParentDao.deleteAllCategories() }
                .compose(SchedulerUtils.ioToMain<CategoryChild>())
    }
}