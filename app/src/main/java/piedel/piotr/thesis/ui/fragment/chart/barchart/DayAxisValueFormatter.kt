package piedel.piotr.thesis.ui.fragment.chart.barchart

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.Calendar
import java.util.Date


class DayAxisValueFormatter(private val chart: BarLineChartBase<*>, private val chosenDate: Date?) : ValueFormatter() {

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {

        val days = value.toInt()

        val calendarInstance = Calendar.getInstance()

        var year: Int = calendarInstance.get(Calendar.YEAR) // Default is current  month
        var month: Int = calendarInstance.get(Calendar.MONTH) // Default is current year

        if (chosenDate != null) { // If date chosen -> change to chosen date
            calendarInstance.time = chosenDate
            month = calendarInstance.get(Calendar.MONTH)
            year = calendarInstance.get(Calendar.YEAR)
        }

        val monthName = mMonths[month % mMonths.size]
        val yearName = year.toString()

        return if (chart.visibleXRange > 30 * 6) {
            "$monthName $yearName"
        } else {
            if (days == 0) "" else days.toString() + " " + monthName
        }
    }

    private fun getDaysOfMonth(calendarInstance: Calendar, chosenDate: Date?): Int {
        calendarInstance.time = chosenDate
        calendarInstance.set(Calendar.YEAR, calendarInstance.get(Calendar.YEAR))
        calendarInstance.set(Calendar.MONTH, calendarInstance.get(Calendar.MONTH))
        return calendarInstance.getActualMaximum(Calendar.DATE)
    }

    private val mMonths = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
}