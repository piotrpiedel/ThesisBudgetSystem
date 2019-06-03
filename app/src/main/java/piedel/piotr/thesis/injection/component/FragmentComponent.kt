package piedel.piotr.thesis.injection.component

import dagger.Subcomponent
import piedel.piotr.thesis.injection.module.FragmentModule
import piedel.piotr.thesis.injection.scopes.PerFragment
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.category.categorylist.CategoryFragment
import piedel.piotr.thesis.ui.fragment.category.categoryselectlist.CategorySelectListFragment
import piedel.piotr.thesis.ui.fragment.chart.barchart.BarChartFragment
import piedel.piotr.thesis.ui.fragment.chart.choosechart.ChooseChartFragment
import piedel.piotr.thesis.ui.fragment.chart.piechart.PieCharFragment
import piedel.piotr.thesis.ui.fragment.importexport.importfromhtml.ImportExportFragment
import piedel.piotr.thesis.ui.fragment.ocr.googledrive.ImportFromImageDriveFragment
import piedel.piotr.thesis.ui.fragment.operation.operationaddview.AddOperationFragment
import piedel.piotr.thesis.ui.fragment.operation.operationlist.OperationFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptadd.ReceiptAddFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptlist.ReceiptFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptpreview.ReceiptPreviewFragment

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(baseFragment: BaseFragment)

    fun inject(operationFragment: OperationFragment)

    fun inject(addOperationFragment: AddOperationFragment)

    fun inject(categoriesFragment: CategoryFragment)

    fun inject(importExportFragment: ImportExportFragment)

    fun inject(categorySelectListFragment: CategorySelectListFragment)

    fun inject(receiptFragment: ReceiptFragment)

    fun inject(receiptAddFragment: ReceiptAddFragment)

    fun inject(receiptPreviewFragment: ReceiptPreviewFragment)

    fun inject(barChartFragment: BarChartFragment)

    fun inject(chooseChartFragment: ChooseChartFragment)

    fun inject(pieCharFragment: PieCharFragment)

    fun inject(importFromImageDriveFragment: ImportFromImageDriveFragment)

}