package piedel.piotr.thesis.ui.fragment.chart.piechart

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog
import piedel.piotr.thesis.R
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.getColorsFromTemplate
import piedel.piotr.thesis.util.showToast
import piedel.piotr.thesis.util.simpleDateMonthYearFormat
import java.util.Calendar
import javax.inject.Inject


class PieCharFragment : BaseFragment(), PieChartView {

    @Inject
    lateinit var pieChartPresenter: PieChartPresenter

    @BindView(R.id.chart_calendar_container)
    lateinit var linearLayout: LinearLayout

    @BindView(R.id.chart_month_date)
    lateinit var textViewDate: TextView

    @BindView(R.id.bar_chart_layout)
    lateinit var chart: PieChart

    override val layout: Int
        get() = R.layout.fragment_pie_chart

    override val toolbarTitle: String
        get() = context?.getString(R.string.pie_chart).toString()

    private val calendarInstance: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        pieChartPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePickerDialog()
        pieChartPresenter.initFragment(calendarInstance)

    }

    override fun setPieChart() {
        val description = Description()
        description.text = "Categories - outcome"
        chart.description = description
    }

    private fun setDatePickerDialog() {
        setOnDateClickListener()
    }

    private fun setOnDateClickListener() {
        linearLayout.setOnClickListener {
            YearMonthPickerDialog(requireContext(), YearMonthPickerDialog.OnDateSetListener { years, month ->
                setSelectedDate(years, month)
                val dateFormat = simpleDateMonthYearFormat()
                textViewDate.text = (dateFormat.format(calendarInstance.time))
                updateDataBySelectedMonthAndYear(calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.YEAR))
            }).show()
        }
    }

    private fun updateDataBySelectedMonthAndYear(selectedMonth: Int, selectedYear: Int) {
        pieChartPresenter.updateDataBySelectedMonthAndYear(selectedMonth, selectedYear)
    }

    override fun loadDataToPieChart(entries: ArrayList<PieEntry>) {
        setData(entries)
    }

    private fun setData(entries: ArrayList<PieEntry>) {
        val dataSet = PieDataSet(entries, getString(R.string.categories_pie_chart))
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors = getColorsFromTemplate() // add a lot of colors
        dataSet.colors = colors
        val data = PieData(dataSet)

        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        chart.data = data

        // undo all highlights
        chart.highlightValues(null)
        chart.invalidate()
    }

    private fun setSelectedDate(years: Int, monthOfYear: Int) {
        calendarInstance.set(Calendar.YEAR, years)
        calendarInstance.set(Calendar.MONTH, monthOfYear)
    }

    override fun setDateToThisMonth() {
        val calendar = Calendar.getInstance()
        val dateFormat = simpleDateMonthYearFormat()
        textViewDate.text = (dateFormat.format(calendar.time))
    }

    override fun showError(throwable: Throwable?) {
        showToast(requireContext(), throwable.toString())
    }

    companion object {
        const val FRAGMENT_TAG: String = "PieChartFragment"
    }

}