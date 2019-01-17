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

    @Insert(onConflict = OnConflictStrategy.REPLACE)     //    @WorkerThread
    fun insertOperation(operation: Operation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)     //    @WorkerThread
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

    @Query("SELECT date, SUM(value)  as sumValueForCategory, categoryId, category_title FROM operation_table, category_table WHERE strftime('%m', date) =:month  AND strftime('%Y', date) =:year AND other_category_id=categoryId GROUP BY categoryId ")
    fun selectSummaryOperationByCategoryMonthly(month: String, year: String): Maybe<List<DateValueCategoryTuple>>

//    @Query("SELECT date, SUM(value) as sumValueForDate FROM operation_table WHERE strftime('%m', `date`) =:month  AND strftime('%Y', `date`) =:year GROUP BY date ORDER BY date ASC  ")
//    fun selectSumOfOperationByDateMonthly(month: Int, year: Int): Maybe<List<DateValueTuple>>

    @Query("SELECT * FROM operation_table LEFT JOIN category_table ON other_category_id == categoryId  ")
    fun selectAllOperationsWithCategories(): Maybe<List<OperationCategoryTuple>>

    @Query("SELECT value, operationType from operation_table")
    fun selectValueOperationList(): Maybe<List<OperationValueOperationType>>

    @Query("DELETE FROM operation_table")
    fun deleteAllOperations()

}