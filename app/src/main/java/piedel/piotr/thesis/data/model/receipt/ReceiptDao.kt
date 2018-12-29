package piedel.piotr.thesis.data.model.receipt

import androidx.room.*
import io.reactivex.Maybe

@Dao
interface ReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)     //    @WorkerThread
    fun insertReceipt(Receipt: Receipt): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)     //    @WorkerThread
    fun insertReceipt(vararg Receipt: Receipt)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateReceipt(Receipt: Receipt)

    @Delete
    fun deleteReceipt(Receipt: Receipt)

    @Query("SELECT * from receipt_table WHERE id =:idReceiptOther")
    fun selectReceipt(idReceiptOther: Int): Maybe<Receipt>

    @Query("SELECT * FROM receipt_table")
    fun selectAllReceipts(): Maybe<MutableList<Receipt>>

    @Query("SELECT * FROM receipt_table")
    fun selectAllReceiptsWithCategories(): Maybe<MutableList<Receipt>>

//    @Query("SELECT value,ReceiptType from Receipt_table")
//    fun selectValueReceiptList(): Maybe<List<ReceiptValueReceiptType>>

    @Query("DELETE FROM receipt_table")
    fun deleteAllReceipts()
}