package piedel.piotr.thesis.features.detail

import piedel.piotr.thesis.data.model.Pokemon
import piedel.piotr.thesis.data.model.Statistic
import piedel.piotr.thesis.features.base.MvpView

interface DetailMvpView : MvpView {

    fun showPokemon(pokemon: Pokemon)

    fun showStat(statistic: Statistic)

    fun showProgress(show: Boolean)

    fun showError(error: Throwable)

}