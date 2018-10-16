package piedel.piotr.thesis.injection.module

import android.app.Activity
import android.content.Context

import dagger.Module
import dagger.Provides
import piedel.piotr.thesis.injection.scopes.ActivityContext

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    internal fun provideActivity(): Activity {
        return activity
    }

    @Provides
    @ActivityContext
    internal fun providesContext(): Context {
        return activity
    }
}
