package piedel.piotr.thesis.data.model.category.categoryparent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Maybe

@Dao
interface CategoryParentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryParent: CategoryParent);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategory(vararg categoryParent: CategoryParent)

    @Update
    fun updateCategory(categoryParent: CategoryParent)

    @Delete
    fun deleteCategory(categoryParent: CategoryParent)

    @Query("SELECT * from category_parent_table WHERE category_id_parent =:idCategoryOther")
    fun selectCategory(idCategoryOther: Int): Maybe<List<CategoryParent>>

    @Query("SELECT * FROM category_parent_table")
    fun selectAllCategories(): Maybe<List<CategoryParent>>

    @Query("DELETE FROM category_parent_table")
    fun deleteAllCategories()
}