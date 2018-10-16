package piedel.piotr.thesis.ui.detail

import piedel.piotr.thesis.data.model.Pokemon
import piedel.piotr.thesis.data.model.Statistic
import piedel.piotr.thesis.ui.base.BaseView

interface DetailMvpView : BaseView {

    fun showPokemon(pokemon: Pokemon)

    fun showStat(statistic: Statistic)

    fun showProgress(show: Boolean)

    fun showError(error: Throwable)

}