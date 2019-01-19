package piedel.piotr.thesis.data.model.category.categoryparent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Maybe
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild

@Dao
interface CategoryParentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryChild: CategoryChild);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategory(vararg categoryChild: CategoryChild)

    @Update
    fun updateCategory(categoryChild: CategoryChild)

    @Delete
    fun deleteCategory(categoryChild: CategoryChild)

    @Query("SELECT * from category_parent_table WHERE categoryId =:idCategoryOther")
    fun selectCategory(idCategoryOther: Int): Maybe<List<CategoryChild>>

//    @Query("SELECT * from category_parent_table WHERE categoryId =:idCategoryOther AND parentCategoryId =:parentCategoryOther")
//    fun selectChildCategory(idCategoryOther: Int, parentCategoryOther: Int): Maybe<List<CategoryChild>>
//
//    @Query("SELECT * from category_parent_table WHERE parentCategoryId =:parentCategoryOther")
//    fun selectAllChildFromParentCategory(parentCategoryOther: Int): Maybe<List<CategoryChild>>
//
//    @Query("SELECT * from category_parent_table WHERE parentCategoryId IS NOT NULL")
//    fun selectAllChildFromParents(): Maybe<List<CategoryChild>>

    @Query("SELECT * FROM category_parent_table")
    fun selectAllCategories(): Maybe<List<CategoryChild>>

//    @Query("SELECT * FROM category_parent_table WHERE parentCategoryId IS NULL")
//    fun selectParentCategories(): Maybe<List<CategoryChild>>


    @Query("DELETE FROM category_parent_table")
    fun deleteAllCategories()
}