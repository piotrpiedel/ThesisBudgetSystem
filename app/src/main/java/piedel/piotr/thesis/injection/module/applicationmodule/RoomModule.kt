package piedel.piotr.thesis.injection.module.applicationmodule

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import piedel.piotr.thesis.data.AppDatabase
import piedel.piotr.thesis.data.model.category.CategoryDao
import piedel.piotr.thesis.data.model.operation.OperationDao
import piedel.piotr.thesis.data.model.receipt.ReceiptDao
import piedel.piotr.thesis.injection.scopes.ApplicationScope
import piedel.piotr.thesis.util.ioThread

@Module
class RoomModule(application: Application) {

    private var appDatabase: AppDatabase

    init {
        appDatabase = Room.databaseBuilder(application, AppDatabase::class.java, "app_database")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        ioThread {
                            //                            Timber.d("iothread" + arrayOfCategories.contentToString())
//                            Timber.d("iothread" + arrayOfFoodSubcategories.contentToString())
//                            Timber.d("iothread" + arrayOfentertainmentsSubcategories.contentToString())

                            providesRoomDatabase().getCategoryDao().insertAllCategory(*CategoryList().getListOfParentCategories(),
                                    *CategoryList().getListOfFoodSubcategories(),
                                    *CategoryList().getListOfEntertainmentSubcategories())
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
    }

    @ApplicationScope
    @Provides
    fun providesRoomDatabase(): AppDatabase {
        return this.appDatabase
    }

    @ApplicationScope
    @Provides
    fun providesCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.getCategoryDao()
    }

    @ApplicationScope
    @Provides
    fun providesOperationDao(appDatabase: AppDatabase): OperationDao {
        return appDatabase.getOperationDao()
    }

    @ApplicationScope
    @Provides
    fun providesReceiptDao(appDatabase: AppDatabase): ReceiptDao {
        return appDatabase.getReceiptDao()
    }

}