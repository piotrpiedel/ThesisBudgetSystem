package piedel.piotr.thesis.injection.component

import piedel.piotr.thesis.injection.PerFragment
import piedel.piotr.thesis.injection.module.FragmentModule
import dagger.Subcomponent

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent