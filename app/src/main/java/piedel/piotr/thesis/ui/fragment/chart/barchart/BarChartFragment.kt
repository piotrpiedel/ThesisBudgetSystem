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
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.setColorDataSetBarChart
import piedel.piotr.thesis.util.showToast
import piedel.piotr.thesis.util.simpleDateMonthYearFormat
import java.util.*
import javax.inject.Inject


class BarChartFragment : BaseFragment(), BarCharContract.BarChartView {

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
    }

    override fun setBarChart() {
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
                val chosenDate: Date? = cal.time
                val dateFormat = simpleDateMonthYearFormat()
                textViewDate.text = (dateFormat.format(cal.time))
                setNewDataAfterChoiceOfDate(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
                setNewValueFormatter(chosenDate)
            }).show();
        }
    }

    private fun setNewDataAfterChoiceOfDate(selectedMonth: Int, selectedYear: Int) {
        barChartPresenter.loadBarChartDataMonthlyAfterDateSelected(selectedMonth, selectedYear)
    }

    private fun setNewValueFormatter(chosenDate: Date?) {
        val xAxisFormatter = DayAxisValueFormatter(chart, chosenDate)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = xAxisFormatter
        chart.invalidate()
    }

    private fun setSelectedDate(cal: Calendar, years: Int, monthOfYear: Int) {
        cal.set(Calendar.YEAR, years)
        cal.set(Calendar.MONTH, monthOfYear)
    }

    override fun setChartData(barChartEntries: MutableList<BarEntry>) {
        val set = BarDataSet(barChartEntries, getString(R.string.summar_of_income_outcome_per_day))
        setColorDataSetBarChart(set, requireContext())
        val barData = BarData(set)
        barData.barWidth = 0.9f // set custom bar width
        chart.data = barData
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.fitScreen()
        chart.setVisibleXRangeMaximum(7f)
        chart.invalidate()
    }

    private fun setBarChartXAxis() {
        val xAxisFormatter = DayAxisValueFormatter(chart, null)
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