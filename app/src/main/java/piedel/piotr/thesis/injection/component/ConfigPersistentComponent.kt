package piedel.piotr.thesis.injection.component

import dagger.Component
import piedel.piotr.thesis.ui.base.BaseActivity
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.injection.module.ActivityModule
import piedel.piotr.thesis.injection.module.FragmentModule

/**
 * A dagger component that will live during the lifecycle of an Activity or Fragment but it won't
 * be destroy during configuration changes. Check [BaseActivity] and [BaseFragment] to
 * see how this components survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

}
