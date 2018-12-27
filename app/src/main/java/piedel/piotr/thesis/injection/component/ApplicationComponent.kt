package piedel.piotr.thesis.injection.component

import android.app.Application
import android.content.Context
import dagger.Component
import piedel.piotr.thesis.data.AppDatabase
import piedel.piotr.thesis.data.model.category.CategoryDao
import piedel.piotr.thesis.data.model.operation.OperationDao
import piedel.piotr.thesis.data.model.receipt.ReceiptDao
import piedel.piotr.thesis.injection.module.applicationmodule.AppModule
import piedel.piotr.thesis.injection.module.applicationmodule.RoomModule
import piedel.piotr.thesis.injection.scopes.ApplicationContext
import piedel.piotr.thesis.injection.scopes.ApplicationScope

@ApplicationScope
@Component(modules = [AppModule::class, RoomModule::class])
interface ApplicationComponent {

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun appDatabase(): AppDatabase

    fun categoryDao(): CategoryDao

    fun operationDao(): OperationDao

    fun receiptDao(): ReceiptDao
}
