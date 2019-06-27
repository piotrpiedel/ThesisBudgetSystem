package piedel.piotr.thesis.ui.activity.main

import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.activity.main.MainContract.MainView
import piedel.piotr.thesis.ui.activity.main.MainContract.PresenterContract
import javax.inject.Inject

@ConfigPersistent
class MainPresenter @Inject
constructor() : BasePresenter<MainView>(), PresenterContract<MainView> {

    override fun initStartingFragment() {
        checkViewAttached()
        view?.initFirstFragment()
    }
}