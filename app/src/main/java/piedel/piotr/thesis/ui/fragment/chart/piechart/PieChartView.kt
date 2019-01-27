package piedel.piotr.thesis.ui.fragment.chart.piechart

import com.github.mikephil.charting.data.PieEntry
import piedel.piotr.thesis.ui.base.BaseView

interface PieChartView : BaseView {

    fun setDateToThisMonth()

    fun loadDataToPieChart(entries: ArrayList<PieEntry>)

    fun showError(throwable: Throwable?)

    fun setPieChart()

}