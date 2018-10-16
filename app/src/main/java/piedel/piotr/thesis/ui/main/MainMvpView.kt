package piedel.piotr.thesis.ui.main

import piedel.piotr.thesis.ui.base.BaseView

interface MainMvpView : BaseView {

    fun showPokemon(pokemon: List<String>)

    fun showProgress(show: Boolean)

    fun showError(error: Throwable)

}