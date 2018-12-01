package piedel.piotr.thesis.injection.component

import dagger.Subcomponent
import piedel.piotr.thesis.injection.module.ActivityModule
import piedel.piotr.thesis.injection.scopes.PerActivity
import piedel.piotr.thesis.ui.base.BaseActivity
import piedel.piotr.thesis.ui.main.MainActivity

@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(baseActivity: BaseActivity)

    fun inject(mainActivity: MainActivity)
}
