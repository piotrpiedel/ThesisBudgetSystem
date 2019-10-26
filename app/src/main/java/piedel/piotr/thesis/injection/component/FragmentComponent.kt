package piedel.piotr.thesis.injection.component

import dagger.Subcomponent
import piedel.piotr.thesis.injection.module.FragmentModule
import piedel.piotr.thesis.injection.scopes.PerFragment
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.category.categorylist.CategoryListFragment
import piedel.piotr.thesis.ui.fragment.category.categoryselectlablelist.CategorySelectableListFragment
import piedel.piotr.thesis.ui.fragment.chart.barchart.BarChartFragment
import piedel.piotr.thesis.ui.fragment.chart.choosechart.ChooseChartFragment
import piedel.piotr.thesis.ui.fragment.chart.piechart.PieCharFragment
import piedel.piotr.thesis.ui.fragment.importexport.importfromhtml.ImportExportFragment
import piedel.piotr.thesis.ui.fragment.ocr.googledrive.ImportFromImageDriveFragment
import piedel.piotr.thesis.ui.fragment.operation.operationselectablelist.OperationSelectableListFragment
import piedel.piotr.thesis.ui.fragment.operation.operationaddnew.OperationAddNewFragment
import piedel.piotr.thesis.ui.fragment.operation.operationlist.OperationListFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptadd.ReceiptAddNewFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptlist.ReceiptListFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptpreview.ReceiptPreviewFragment

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(baseFragment: BaseFragment)

    fun inject(operationListFragment: OperationListFragment)

    fun inject(operationAddNewFragment: OperationAddNewFragment)

    fun inject(categoriesListFragment: CategoryListFragment)

    fun inject(importExportFragment: ImportExportFragment)

    fun inject(categorySelectableListFragment: CategorySelectableListFragment)

    fun inject(receiptListFragment: ReceiptListFragment)

    fun inject(receiptAddNewFragment: ReceiptAddNewFragment)

    fun inject(receiptPreviewFragment: ReceiptPreviewFragment)

    fun inject(barChartFragment: BarChartFragment)

    fun inject(chooseChartFragment: ChooseChartFragment)

    fun inject(pieCharFragment: PieCharFragment)

    fun inject(importFromImageDriveFragment: ImportFromImageDriveFragment)

    fun inject(operationSelectableListFragment: OperationSelectableListFragment)

}