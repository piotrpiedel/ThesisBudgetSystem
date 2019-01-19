package piedel.piotr.thesis.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChildDao
import piedel.piotr.thesis.data.model.category.categoryparent.CategoryParent
import piedel.piotr.thesis.data.model.category.categoryparent.CategoryParentDao
import piedel.piotr.thesis.data.model.converters.DateConverter
import piedel.piotr.thesis.data.model.converters.OperationTypeConverter
import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationDao
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.data.model.receipt.ReceiptDao
import piedel.piotr.thesis.injection.scopes.ApplicationScope

@ApplicationScope
@Database(entities = arrayOf(CategoryChild::class, CategoryParent::class, Operation::class, Receipt::class), version = 1)
@TypeConverters(DateConverter::class, OperationTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCategoryChildDao(): CategoryChildDao

    abstract fun getCategoryParentDao(): CategoryParentDao

    abstract fun getOperationDao(): OperationDao

    abstract fun getReceiptDao(): ReceiptDao

}