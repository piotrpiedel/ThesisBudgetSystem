package piedel.piotr.thesis.injection.component

import piedel.piotr.thesis.injection.scopes.PerFragment
import piedel.piotr.thesis.injection.module.FragmentModule
import dagger.Subcomponent
import piedel.piotr.thesis.ui.base.BaseFragment

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent{

    fun inject(baseFragment : BaseFragment)
}