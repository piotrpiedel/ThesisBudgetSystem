package piedel.piotr.thesis.data.model.operation

import android.arch.persistence.room.*
import io.reactivex.Maybe
import io.reactivex.Single

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

    @Query("SELECT value,operationType from operation_table")
    fun selectValueOperationList(): Maybe<List<OperationValueOperationType>>

    @Query("DELETE FROM operation_table")
    fun deleteAllOperations()

}