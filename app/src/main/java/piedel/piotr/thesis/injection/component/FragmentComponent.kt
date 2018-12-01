package piedel.piotr.thesis.injection.component

import dagger.Subcomponent
import piedel.piotr.thesis.injection.module.FragmentModule
import piedel.piotr.thesis.injection.scopes.PerFragment
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.category.categorylist.CategoryFragment
import piedel.piotr.thesis.ui.fragment.operation.operationaddview.AddOperationFragment
import piedel.piotr.thesis.ui.fragment.operation.operationlist.OperationFragment

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun inject(baseFragment: BaseFragment)

    fun inject(operationFragment: OperationFragment)

    fun inject(addOperationFragment: AddOperationFragment)

    fun inject(categoriesFragment: CategoryFragment)

}