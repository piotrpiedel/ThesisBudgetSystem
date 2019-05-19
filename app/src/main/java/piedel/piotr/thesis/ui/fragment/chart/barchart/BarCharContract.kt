package piedel.piotr.thesis.ui.fragment.chart.barchart

import com.github.mikephil.charting.data.BarEntry
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface BarCharContract {

    interface BarChartView : BaseView {

        fun showError(throwable: Throwable?)

        fun setDateToThisMonth()

        fun setChartData(barChartEntries: MutableList<BarEntry>)

        fun setBarChart()
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initFragment()

        fun loadBarChartDataMonthlyInitially()

        fun loadBarChartDataMonthlyAfterDateSelected(selectedMonth: Int, selectedYear: Int)
    }

}