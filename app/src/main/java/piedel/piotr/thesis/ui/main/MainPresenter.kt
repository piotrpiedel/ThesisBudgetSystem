package piedel.piotr.thesis.ui.main

import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.base.BasePresenter
import javax.inject.Inject

@ConfigPersistent
class MainPresenter @Inject
constructor() : BasePresenter<MainView>() {

    fun initStartingFragment() {
        checkViewAttached()
        view?.initFirstFragment()
    }


    //    fun getPokemon(limit: Int) {
//        checkViewAttached()
//        view?.showProgress(true)
//        appDatabase.getPokemonList(limit)
//                .compose(SchedulerUtils.ioToMain<List<String>>())
//                .subscribe({ pokemons ->
//                    view?.showProgress(false)
//                    view?.showPokemon(pokemons)
//                }) { throwable ->
//                    view?.showProgress(false)
//                    view?.showError(throwable)
//                }
//    }

}