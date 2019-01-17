package piedel.piotr.thesis.ui.fragment.chart.piechart

import piedel.piotr.thesis.data.model.operation.DateValueCategoryTuple
import piedel.piotr.thesis.ui.base.BaseView

interface PieChartView : BaseView {

    fun setDateToThisMonth()

    fun loadDataToPieChart(selectedData: List<DateValueCategoryTuple>?)

    fun showError(throwable: Throwable?)

}