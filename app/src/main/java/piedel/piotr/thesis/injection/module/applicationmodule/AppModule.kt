package piedel.piotr.thesis.injection.module.applicationmodule

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import piedel.piotr.thesis.injection.scopes.ApplicationContext

@Module
class AppModule(private val application: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return application
    }

    
}