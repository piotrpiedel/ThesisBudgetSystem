package piedel.piotr.thesis.data.model.category.categorychild

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import piedel.piotr.thesis.data.model.category.categoryparent.CategoryParent

@Dao
interface CategoryChildDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryChild: CategoryChild);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategory(vararg categoryChild: CategoryChild)

    @Update
    fun updateCategory(categoryChild: CategoryChild)

    @Delete
    fun deleteCategory(categoryChild: CategoryChild)

    @Query("SELECT * from category_child_table WHERE categoryId =:idCategoryOther")
    fun selectCategory(idCategoryOther: Int): Maybe<List<CategoryChild>>

    @Query("SELECT * from category_child_table WHERE categoryId =:idCategoryOther AND parentCategoryId =:parentCategoryOther")
    fun selectChildCategory(idCategoryOther: Int, parentCategoryOther: Int): Maybe<List<CategoryChild>>

    @Query("SELECT * from category_child_table WHERE parentCategoryId =:parentCategoryOther")
    fun selectAllChildFromParentCategory(parentCategoryOther: Int): Observable<List<CategoryChild>>

    @Query("SELECT * from category_child_table WHERE parentCategoryId IS NOT NULL")
    fun selectAllChildFromParents(): Observable<List<CategoryChild>>

    @Query("SELECT * FROM category_child_table")
    fun selectChildCategories(): Flowable<List<CategoryChild>>

    @Query("SELECT * FROM category_parent_table ")
    fun selectParentCategories(): Observable<List<CategoryParent>>

    @Query("SELECT categoryId,child.category_title, parentCategoryId, category_id_parent, parent.category_title_parent,COUNT(category_id_parent) as countParent FROM category_parent_table as parent LEFT JOIN category_child_table as child  ON child.parentCategoryId== parent.category_id_parent ")
    fun selectCategoriesDividedByParents(): Maybe<List<CategoryChildParentTuple>>

    @Query("DELETE FROM category_child_table")
    fun deleteAllCategories()
}
