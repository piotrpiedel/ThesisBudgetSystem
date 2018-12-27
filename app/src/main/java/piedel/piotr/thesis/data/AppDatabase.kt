package piedel.piotr.thesis.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.data.model.category.CategoryDao
import piedel.piotr.thesis.data.model.converters.DateConverter
import piedel.piotr.thesis.data.model.converters.OperationTypeConverter
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationDao
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.data.model.receipt.ReceiptDao
import piedel.piotr.thesis.injection.scopes.ApplicationScope

@ApplicationScope
@Database(entities = arrayOf(Category::class, Operation::class, Receipt::class), version = 1)
@TypeConverters(DateConverter::class, OperationTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao

    abstract fun getOperationDao(): OperationDao

    abstract fun getReceiptDao(): ReceiptDao

}