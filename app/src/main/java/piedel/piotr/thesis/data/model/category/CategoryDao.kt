package piedel.piotr.thesis.data.model.category

import androidx.room.*
import io.reactivex.Maybe

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategory(vararg category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Query("SELECT * from category_table WHERE categoryId =:idCategoryOther")
    fun selectCategory(idCategoryOther: Int): Maybe<List<Category>>

    @Query("SELECT * from category_table WHERE categoryId =:idCategoryOther AND parentCategoryId =:parentCategoryOther")
    fun selectChildCategory(idCategoryOther: Int, parentCategoryOther: Int): Maybe<List<Category>>

    @Query("SELECT * from category_table WHERE parentCategoryId =:parentCategoryOther")
    fun selectAllChildFromParentCategory(parentCategoryOther: Int): Maybe<List<Category>>

    @Query("SELECT * from category_table WHERE parentCategoryId IS NOT NULL")
    fun selectAllChildFromParents(): Maybe<List<Category>>

    @Query("SELECT * FROM category_table")
    fun selectAllCategories(): Maybe<List<Category>>

    @Query("SELECT * FROM category_table WHERE parentCategoryId IS NULL")
    fun selectParentCategories(): Maybe<List<Category>>


    @Query("DELETE FROM category_table")
    fun deleteAllCategories()
}
