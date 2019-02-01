package piedel.piotr.thesis.ui.fragment.chart.barchart

import android.annotation.SuppressLint
import com.github.mikephil.charting.data.BarEntry
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.category.CategoryRepository
import piedel.piotr.thesis.data.model.operation.DateValueTuple
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


@ConfigPersistent
class BarChartPresenter @Inject
constructor(private val operationRepository: OperationRepository, private val categoryRepository: CategoryRepository) : BasePresenter<BarChartView>() {

    private var disposable: Disposable? = null
    private val calendar = Calendar.getInstance()

    fun initFragment() {
        checkViewAttached()
        view?.setBarChart()
        view?.setDateToThisMonth()
        loadBarChartDataMonthlyInitially()
    }

    @SuppressLint("CheckResult")
    fun loadBarChartDataMonthlyInitially() {
        val year: Int = calendar.get(Calendar.YEAR) // Default is current  month
        val month: Int = calendar.get(Calendar.MONTH) + 1 // Default is current  year
        disposable = operationRepository.selectSumOfOperationByDateMonthly(month, year)
                .subscribe({ selectedData ->
                    view?.setChartData(getEntriesForBarChart(selectedData, year, month))
                }, { throwable ->
                    Timber.d(throwable.toString())
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

    @SuppressLint("CheckResult")
    fun loadBarChartDataMonthlyAfterDateSelected(selectedMonth: Int, selectedYear: Int) {
        val fixedMonth = selectedMonth + 1 //cause 0 is January
        disposable = operationRepository.selectSumOfOperationByDateMonthly(fixedMonth, selectedYear)
                .subscribe({ selectedData ->
                    view?.setChartData(getEntriesForBarChart(selectedData, selectedMonth, selectedYear))
                }, { throwable ->
                    Timber.d(throwable.toString())
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

    fun getEntriesForBarChart(selectedData: List<DateValueTuple>, selectedMonth: Int, selectedYear: Int): MutableList<BarEntry> {
        val entries = mutableListOf<BarEntry>()
        if (selectedData.isNotEmpty()) {
            for (items in selectedData) {
                entries.add(BarEntry((getDayOfMonth(items.date)).toFloat(), items.sumValueForDate.toFloat()))
            }
        } else if (calendar.get(Calendar.MONTH) != 1 && selectedMonth != 1) {
            for (i in 1..31 step (10)) {
                entries.add(BarEntry(i.toFloat(), 0f))
            }
        } else if (calendar.get(Calendar.MONTH) == 1 || selectedMonth == 1) {
            for (i in 1..28 step (7)) {
                entries.add(BarEntry(i.toFloat(), 0f))
            }
        }
        return entries
    }

    private fun getDayOfMonth(aDate: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = aDate
        return cal.get(Calendar.DAY_OF_MONTH)
    }

}