package piedel.piotr.thesis.injection.module

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment

import dagger.Module
import dagger.Provides
import piedel.piotr.thesis.injection.scopes.ActivityContext

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    internal fun providesFragment(): Fragment {
        return fragment
    }

    @Provides
    internal fun provideActivity(): Activity {
        return fragment.requireActivity()
    }

    @Provides
    @ActivityContext
    internal fun providesContext(): Context {
        return fragment.requireContext()
    }

}