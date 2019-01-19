package piedel.piotr.thesis.data.model.category.categorychild

import androidx.room.*
import io.reactivex.Maybe

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
    fun selectAllChildFromParentCategory(parentCategoryOther: Int): Maybe<List<CategoryChild>>

    @Query("SELECT * from category_child_table WHERE parentCategoryId IS NOT NULL")
    fun selectAllChildFromParents(): Maybe<List<CategoryChild>>

    @Query("SELECT * FROM category_child_table")
    fun selectAllCategories(): Maybe<List<CategoryChild>>

    @Query("SELECT * FROM category_child_table WHERE parentCategoryId IS NULL")
    fun selectParentCategories(): Maybe<List<CategoryChild>>


    @Query("DELETE FROM category_child_table")
    fun deleteAllCategories()
}
