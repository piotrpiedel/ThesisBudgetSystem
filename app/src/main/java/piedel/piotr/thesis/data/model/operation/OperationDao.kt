package piedel.piotr.thesis.data.model.operation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Maybe

@Dao
interface OperationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperation(operation: Operation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperation(vararg operation: Operation)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateOperation(operation: Operation)

    @Delete
    fun deleteOperation(operation: Operation)

    @Query("SELECT * from operation_table WHERE id =:idOperationOther")
    fun selectOperation(idOperationOther: Int): Maybe<Operation>

    @Query("SELECT * FROM operation_table")
    fun selectAllOperations(): Maybe<List<Operation>>

    @Query("SELECT date, SUM(value) as sumValueForDate FROM operation_table GROUP BY date ORDER BY date ASC")
    fun selectSumOfOperationByDate(): Maybe<List<DateValueTuple>>

    @Query("SELECT date, SUM(value) as sumValueForDate FROM operation_table WHERE strftime('%m', date) =:month  AND strftime('%Y', date) =:year GROUP BY date ")
    fun selectSumOfOperationByDateMonthly(month: String, year: String): Maybe<List<DateValueTuple>>

    @Query("SELECT date, SUM(value)  as sumValueForCategory, category_id_parent, category_title_parent FROM operation_table, category_child_table,category_parent_table WHERE strftime('%m', date) =:month  AND strftime('%Y', date) =:year AND other_category_id=categoryId AND parentCategoryId==category_id_parent GROUP BY category_id_parent ")
    fun selectSummaryOperationByCategoryMonthly(month: String, year: String): Maybe<List<DateValueCategoryTuple>>

    @Query("SELECT date, SUM(value)  as sumValueForCategory, category_id_parent, category_title_parent FROM operation_table, category_child_table,category_parent_table WHERE strftime('%m', date) =:month  AND strftime('%Y', date) =:year AND other_category_id=categoryId AND parentCategoryId==category_id_parent AND operationType LIKE '%OUTCOME%' GROUP BY category_id_parent ")
    fun selectSummaryOperationByCategoryMonthlyOnlyOutcome(month: String, year: String): Maybe<List<DateValueCategoryTuple>>

    @Query("SELECT * FROM operation_table LEFT JOIN category_child_table ON other_category_id == categoryId  ")
    fun selectAllOperationsWithCategories(): Maybe<List<OperationCategoryTuple>>

    @Query("SELECT * FROM operation_table LEFT JOIN category_child_table ON other_category_id == categoryId  order by date desc")
    fun selectAllOperationsWithCategoriesOrderByDateDesc(): Maybe<List<OperationCategoryTuple>>

    @Query("SELECT value, operationType from operation_table")
    fun selectValueOperationList(): Maybe<List<OperationValueOperationType>>

    @Query("delete from operation_table")
    fun deleteAllOperations()

}