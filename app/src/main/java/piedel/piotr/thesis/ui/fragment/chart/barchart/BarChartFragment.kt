package piedel.piotr.thesis.ui.fragment.chart.barchart

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.operation.DateValueTuple
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.showToast
import piedel.piotr.thesis.util.simpleDateMonthYearFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


class BarChartFragment : BaseFragment(), BarChartView {

    @Inject
    lateinit var barChartPresenter: BarChartPresenter

    @BindView(R.id.chart_calendar_container)
    lateinit var linearLayout: LinearLayout

    @BindView(R.id.chart_month_date)
    lateinit var textViewDate: TextView

    @BindView(R.id.bar_chart_layout)
    lateinit var chart: BarChart

    override val layout: Int
        get() = R.layout.fragment_bar_chart

    override val toolbarTitle: String
        get() = context?.getString(R.string.ba_chart).toString()

    private var chosenDate: Date? = null

    private var dataForChart: MutableList<DateValueTuple> = mutableListOf()

    override fun passTheData(passedData: MutableList<DateValueTuple>) {
        dataForChart.clear()
        dataForChart.addAll(passedData)
        setChartData(chart)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        barChartPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePickerDialog()
        barChartPresenter.initFragment()
        setBarChartXAxis()
        setBarChart()
    }

    private fun setBarChart() {
        val description = Description()
        description.text = ""
        chart.description = description
    }


    override fun setDateToThisMonth() {
        val calendar = Calendar.getInstance()
        val dateFormat = simpleDateMonthYearFormat()
        textViewDate.text = (dateFormat.format(calendar.time))
    }

    private fun setDatePickerDialog() {
        val calendar = Calendar.getInstance()
        setOnDateClickListener(calendar)
    }

    private fun setOnDateClickListener(cal: Calendar) {
        linearLayout.setOnClickListener {
            YearMonthPickerDialog(requireContext(), YearMonthPickerDialog.OnDateSetListener { years, month ->
                setSelectedDate(cal, years, month)
                chosenDate = cal.time
                val dateFormat = simpleDateMonthYearFormat()
                textViewDate.text = (dateFormat.format(cal.time))
                setNewDataAfterChoiceOfDate(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
                setNewValueFormatter()
            }).show();
        }
    }

    private fun setNewDataAfterChoiceOfDate(selectedMonth: Int, selectedYear: Int) {
        barChartPresenter.loadBarChartDataMonthlyAfterDateSelected(selectedMonth, selectedYear)
    }

    private fun setNewValueFormatter() {
        val xAxisFormatter = DayAxisValueFormatter(chart, chosenDate)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = xAxisFormatter
        chart.invalidate()
    }

    private fun setSelectedDate(cal: Calendar, years: Int, monthOfYear: Int) {
        cal.set(Calendar.YEAR, years)
        cal.set(Calendar.MONTH, monthOfYear)
    }

    // w zaleznosci od miesiąca wczytywac ilosc dni w miesiącu oraz jednoczesnie zminiac nazwy na axis na ten miesiąc
    private fun setChartData(chart: BarChart) {
        val calendar = Calendar.getInstance()
        val entries = mutableListOf<BarEntry>()
        if (dataForChart.size != 0) {
            for (items in dataForChart) {
                entries.add(BarEntry((getDayOfMonth(items.date)).toFloat(), items.sumValueForDate.toFloat()))
            }
        } else if (calendar.get(Calendar.MONTH) != 1 && chosenDate?.month != 1) {
            for (i in 1..31 step (10)) {
                entries.add(BarEntry(i.toFloat(), 0f))
            }
        } else if (calendar.get(Calendar.MONTH) == 1 || chosenDate?.month == 1) {
            for (i in 1..28 step (7)) {
                entries.add(BarEntry(i.toFloat(), 0f))
            }
        }
        val set = BarDataSet(entries, getString(R.string.summar_of_income_outcome_per_day))
        set.setColors(intArrayOf(R.color.YellowGreen, R.color.Green, R.color.Blue, R.color.BlueViolet, R.color.AliceBlue, R.color.DarkOrchid, R.color.DarkOrange, R.color.Khaki), requireContext())

        val barData = BarData(set)
        barData.barWidth = 0.9f // set custom bar width
        chart.data = barData
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.fitScreen()
        chart.setVisibleXRangeMaximum(7f)
        chart.invalidate()
    }

    private fun getDayOfMonth(aDate: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = aDate
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    private fun setBarChartXAxis() {
        val xAxisFormatter = DayAxisValueFormatter(chart, chosenDate)
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter
    }

    override fun showError(throwable: Throwable?) {
        showToast(requireContext(), throwable.toString())
    }

    companion object {
        const val FRAGMENT_TAG: String = "BarChartFragment"
    }
}