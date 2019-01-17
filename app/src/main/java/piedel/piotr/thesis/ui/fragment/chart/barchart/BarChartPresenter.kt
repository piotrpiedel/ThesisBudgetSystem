package piedel.piotr.thesis.ui.fragment.chart.barchart

import android.annotation.SuppressLint
import io.reactivex.disposables.Disposable
import piedel.piotr.thesis.data.model.category.CategoryRepository
import piedel.piotr.thesis.data.model.operation.DateValueTuple
import piedel.piotr.thesis.data.model.operation.OperationRepository
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.rxutils.scheduler.SchedulerUtils
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject


@ConfigPersistent
class BarChartPresenter @Inject
constructor(private val operationRepository: OperationRepository, private val categoryRepository: CategoryRepository) : BasePresenter<BarChartView>() {

    private var disposable: Disposable? = null

    fun initFragment() {
        checkViewAttached()
        view?.setDateToThisMonth()
        loadBarChartDataMonthlyInitially()
    }

    @SuppressLint("CheckResult")
    fun loadBarChartDataMonthlyInitially() {
        val calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR) // Jeśli nie ma wybranej żadnej daty to będzie domyślna data z obecnego miesiąca
        val month: Int = calendar.get(Calendar.MONTH) + 1 // Jeśli nie ma wybranej żadnej daty to będzie domyślna data z obecnego roku

        Timber.d("Calendar.MONTH: %s, Calendar.YEAR : %s", month, year)

        disposable = operationRepository.selectSumOfOperationByDateMonthly(month, year)
                .compose(SchedulerUtils.ioToMain<List<DateValueTuple>>())
                .subscribe({ passedData ->
                    Timber.d("loadBarChartData $passedData")
                    view?.passTheData(passedData as MutableList)
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

    @SuppressLint("CheckResult")
    fun loadBarChartDataMonthlyAfterDateSelected(selectedMonth: Int, selectedYear: Int) {
        val fixedMonth = selectedMonth + 1 //cause 0 is January
        Timber.d("Calendar.MONTH: %s, Calendar.YEAR : %s", fixedMonth, selectedYear)
        disposable = operationRepository.selectSumOfOperationByDateMonthly(fixedMonth, selectedYear)
                .compose(SchedulerUtils.ioToMain<List<DateValueTuple>>())
                .subscribe({ passedData ->
                    Timber.d("loadBarChartData $passedData")
                    view?.passTheData(passedData as MutableList)
                }, { throwable ->
                    Timber.d(throwable.localizedMessage)
                    view?.showError(throwable)
                }, {
                })
        addDisposable(disposable)
    }

}