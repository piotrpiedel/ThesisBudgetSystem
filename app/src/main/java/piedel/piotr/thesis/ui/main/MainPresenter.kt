package piedel.piotr.thesis.ui.main

import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.ui.main.MainContract.MainView
import piedel.piotr.thesis.ui.main.MainContract.PresenterContract
import javax.inject.Inject

@ConfigPersistent
class MainPresenter @Inject
constructor() : BasePresenter<MainView>(), PresenterContract<MainView> {

    override fun initStartingFragment() {
        checkViewAttached()
        view?.initFirstFragment()
    }
}