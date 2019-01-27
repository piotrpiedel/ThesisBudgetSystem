package piedel.piotr.thesis.ui.fragment.chart.barchart

import com.github.mikephil.charting.data.BarEntry
import piedel.piotr.thesis.ui.base.BaseView

interface BarChartView : BaseView {

    fun showError(throwable: Throwable?)

    fun setDateToThisMonth()

    fun setChartData(barChartEntries: MutableList<BarEntry>)

    fun setBarChart()
}