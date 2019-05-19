package piedel.piotr.thesis.ui.fragment.chart.choosechart

import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.fragment.chart.choosechart.ChooseChartContract.*
import javax.inject.Inject


@ConfigPersistent
class ChooseChartPresenter @Inject constructor() : BasePresenter<ChooseChartView>(), PresenterContract<ChooseChartView> {

}