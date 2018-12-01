package piedel.piotr.thesis.data.model.category

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Maybe
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import piedel.piotr.thesis.util.rxutils.subscriber.CompletableObserverMain
import javax.inject.Inject

@SuppressLint("CheckResult")
class CategoryRepository @Inject constructor(val categoryDao: CategoryDao) {


    fun insertCategory(category: Category) {
        Completable.fromAction { categoryDao.insertCategory(category) }
                .compose(SchedulerUtils.ioToMain<Category>())
                .subscribe(CompletableObserverMain())
    }

    fun insertAllCategory(categories: Array<Category>) {
        Completable.fromAction { categoryDao.insertAllCategory(*categories) }
                .compose(SchedulerUtils.ioToMain<Category>())
                .subscribe(CompletableObserverMain())
    }

    fun updateCategory(category: Category) {
        Completable.fromAction { categoryDao.updateCategory(category) }
                .compose(SchedulerUtils.ioToMain<Category>())
                .subscribe(CompletableObserverMain())
    }

    fun deleteCategory(category: Category) {
        Completable.fromAction { categoryDao.deleteCategory(category) }
                .compose(SchedulerUtils.ioToMain<Category>())
                .subscribe(CompletableObserverMain())
    }

    fun selectCategory(idCategoryOther: Int): Maybe<List<Category>> {
        return categoryDao.selectCategory(idCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<Category>>())
    }

    fun selectChildCategory(idCategoryOther: Int, parentCategoryOther: Int): Maybe<List<Category>> {
        return categoryDao.selectChildCategory(idCategoryOther, parentCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<Category>>())
    }

    fun selectAllChildFromParentCategory(parentCategoryOther: Int): Maybe<List<Category>> {
        return categoryDao.selectAllChildFromParentCategory(parentCategoryOther)
                .compose(SchedulerUtils.ioToMain<List<Category>>())
    }

    fun selectAllCategories(): Maybe<List<Category>> {
        return categoryDao.selectAllCategories()
    }

    fun selectAllChildFromParents(): Maybe<List<Category>> {
        return categoryDao.selectAllChildFromParents()
    }

    fun selectParentCategories(): Maybe<List<Category>> {
        return categoryDao.selectParentCategories()
    }

    fun deleteAllCategories() {
        Completable.fromAction { categoryDao.deleteAllCategories() }
                .compose(SchedulerUtils.ioToMain<Category>())
    }


}