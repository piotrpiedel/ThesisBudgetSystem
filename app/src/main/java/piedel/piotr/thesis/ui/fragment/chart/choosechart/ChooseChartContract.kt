package piedel.piotr.thesis.ui.fragment.chart.choosechart

import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface ChooseChartContract {
    interface ChooseChartView : BaseView {
    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
    }
}