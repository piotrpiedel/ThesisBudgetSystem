package piedel.piotr.thesis.ui.fragment.chart.barchart

import piedel.piotr.thesis.data.model.operation.DateValueTuple
import piedel.piotr.thesis.ui.base.BaseView

interface BarChartView : BaseView {

    fun passTheData(passedData: MutableList<DateValueTuple>)

    fun showError(throwable: Throwable?)

    fun setDateToThisMonth()

}