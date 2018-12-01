package piedel.piotr.thesis.ui.main

import piedel.piotr.thesis.ui.base.BaseView

interface MainView : BaseView {

    fun showProgress(show: Boolean)

    fun initFirstFragment()

    fun showError(error: Throwable)

}