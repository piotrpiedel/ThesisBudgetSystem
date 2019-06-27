package piedel.piotr.thesis.ui.activity.main

import piedel.piotr.thesis.ui.base.BaseView
import piedel.piotr.thesis.ui.base.Presenter

interface MainContract {
    interface MainView : BaseView {

        fun showProgressBar(show: Boolean)

        fun initFirstFragment()

        fun showError(error: Throwable)

    }

    interface PresenterContract<T : BaseView> : Presenter<T> {
        fun initStartingFragment()
    }
}