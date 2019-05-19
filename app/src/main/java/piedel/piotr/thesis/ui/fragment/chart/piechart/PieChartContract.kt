package piedel.piotr.thesis.ui.fragment.chart.piechart

import com.github.mikephil.charting.data.PieEntry
import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter
import java.util.*
import kotlin.collections.ArrayList

interface PieChartContract {
    interface PieChartView : BaseView {
        fun setDateToThisMonth()

        fun loadDataToPieChart(entries: ArrayList<PieEntry>)

        fun showError(throwable: Throwable?)

        fun setPieChart()
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initFragment(calendarInstance: Calendar)

        fun updateDataBySelectedMonthAndYear(selectedMonth: Int, selectedYear: Int)
    }
}