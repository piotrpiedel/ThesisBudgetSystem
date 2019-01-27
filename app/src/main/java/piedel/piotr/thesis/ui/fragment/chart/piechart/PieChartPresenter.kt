package piedel.piotr.thesis.ui.fragment.chart.piechart

import android.annotation.SuppressLint
import com.github.mikephil.charting.data.PieEntry
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.operation.DateValueCategoryTuple
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs

@ConfigPersistent
class PieChartPresenter @Inject constructor(private val operationRepository: OperationRepository) : BasePresenter<PieChartView>() {

    private var disposable: Disposable? = null

    fun initFragment(calendarInstance: Calendar) {
        checkViewAttached()
        view?.setPieChart()
        view?.setDateToThisMonth()
        loadMonthlySummaryByCategory(calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.YEAR))
    }

    @SuppressLint("CheckResult")
    private fun loadMonthlySummaryByCategory(selectedMonth: Int, selectedYear: Int) {
        val fixedMonth = selectedMonth + 1 //cause 0 is January
        disposable = operationRepository.selectSummaryOperationByCategoryMonthlyOnlyOutcome(fixedMonth, selectedYear)
                .subscribe({ selectedData ->
                    Timber.d(selectedData.toString())
                    val entries = arrayListOf<PieEntry>()
                    if (selectedData.isNotEmpty())
                        loadEntries(selectedData, entries)
                    view?.loadDataToPieChart(entries)

                }, { throwable ->
                    Timber.d(throwable.toString())
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

    private fun loadEntries(loadedData: List<DateValueCategoryTuple>, entries: ArrayList<PieEntry>) {
        var summaryValueOfAllItems = 0.0
        for (items in loadedData) {
            summaryValueOfAllItems += abs(items.sumValueForCategory)
        }
        for (items in loadedData) {
            if (items.sumValueForCategory.toInt() != 0)
                entries.add(PieEntry(abs((items.sumValueForCategory / summaryValueOfAllItems * 100).toFloat()), items.category_title_parent))
        }
    }

    fun updateDataBySelectedMonthAndYear(selectedMonth: Int, selectedYear: Int) {
        loadMonthlySummaryByCategory(selectedMonth, selectedYear)
    }
}