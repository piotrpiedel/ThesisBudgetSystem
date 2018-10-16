package piedel.piotr.thesis.common.injection.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import piedel.piotr.thesis.data.DataManager
import piedel.piotr.thesis.data.remote.PokemonApi
import piedel.piotr.thesis.injection.scopes.ApplicationContext
import org.mockito.Mockito.mock
import javax.inject.Singleton

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
class ApplicationTestModule(private val mApplication: Application) {

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    /*************
     * MOCKS
     */

    @Provides
    @Singleton
    internal fun providesDataManager(): DataManager {
        return mock(DataManager::class.java)
    }

    @Provides
    @Singleton
    internal fun provideMvpBoilerplateService(): PokemonApi {
        return mock(PokemonApi::class.java)
    }

}
