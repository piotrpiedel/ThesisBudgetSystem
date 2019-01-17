package piedel.piotr.thesis.ui.fragment.chart.barchart

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.Calendar
import java.util.Date


class DayAxisValueFormatter(private val chart: BarLineChartBase<*>, private val chosenDate: Date?) : IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {

        val days = value.toInt()

        val calendarInstance = Calendar.getInstance()

        var year: Int = calendarInstance.get(Calendar.YEAR) // Jeśli nie ma wybranej żadnej daty to będzie domyślna data z obecnego miesiąca
        var month: Int = calendarInstance.get(Calendar.MONTH) // Jeśli nie ma wybranej żadnej daty to będzie domyślna data z obecnego roku

        if (chosenDate != null) { // Jeśli wybrana  data to będzie  data  wybranej daty
            calendarInstance.time = chosenDate
            month = calendarInstance.get(Calendar.MONTH)
            year = calendarInstance.get(Calendar.YEAR)
        }

        val monthName = mMonths[month % mMonths.size]
        val yearName = year.toString()

        if (chart.visibleXRange > 30 * 6) {
            return "$monthName $yearName"
        } else {
            val dayOfMonth = days
            return if (dayOfMonth == 0) "" else dayOfMonth.toString() + " " + monthName
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