package piedel.piotr.thesis.ui.fragment.chart.piechart

import android.annotation.SuppressLint
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.operation.DateValueCategoryTuple
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@ConfigPersistent
class PieChartPresenter @Inject constructor(private val operationRepository: OperationRepository) : BasePresenter<PieChartView>() {

    private var disposable: Disposable? = null

    fun initFragment(calendarInstance: Calendar) {
        checkViewAttached()
        view?.setDateToThisMonth()
        loadMonthlySummaryByCategory(calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.YEAR))
    }

    @SuppressLint("CheckResult")
    private fun loadMonthlySummaryByCategory(selectedMonth: Int, selectedYear: Int) {
        val fixedMonth = selectedMonth + 1 //cause 0 is January
        Timber.d("Calendar.MONTH: %s, Calendar.YEAR : %s", fixedMonth, selectedYear)
        disposable = operationRepository.selectSummaryOperationByCategoryMonthly(fixedMonth, selectedYear)
                .compose(SchedulerUtils.ioToMain<List<DateValueCategoryTuple>>())
                .subscribe({ selectedData ->
                    Timber.d("loadPieChartData $selectedData")
                    view?.loadDataToPieChart(selectedData)
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

    fun updateDataBySelectedMonthAndYear(selectedMonth: Int, selectedYear: Int) {
        loadMonthlySummaryByCategory(selectedMonth, selectedYear)
    }
}