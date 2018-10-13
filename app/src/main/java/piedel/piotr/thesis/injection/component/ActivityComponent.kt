package piedel.piotr.thesis.injection.component

import piedel.piotr.thesis.injection.PerActivity
import piedel.piotr.thesis.injection.module.ActivityModule
import piedel.piotr.thesis.features.base.BaseActivity
import piedel.piotr.thesis.features.detail.DetailActivity
import piedel.piotr.thesis.features.main.MainActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(baseActivity: BaseActivity)

    fun inject(mainActivity: MainActivity)

    fun inject(detailActivity: DetailActivity)
}
