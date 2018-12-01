//package piedel.piotr.thesis.ui.detail
//
//import piedel.piotr.thesis.data.model.category.CategoryRepository
//import piedel.piotr.thesis.ui.base.BasePresenter
//import piedel.piotr.thesis.injection.scopes.ConfigPersistent
//import javax.inject.Inject
//
//@ConfigPersistent
//class DetailPresenter @Inject
//constructor(private val categoryRepository: CategoryRepository) : BasePresenter<DetailView>() {
//
//
//    //    fun getPokemon(name: String) {
////        checkViewAttached()
////        view?.showProgress(true)
////        appDatabase.getPokemon(name)
////                .compose(SchedulerUtils.ioToMain<Pokemon>())
////                .subscribe({ pokemon ->
////                    // It should be always checked if BaseView (Fragment or Activity) is attached.
////                    // Calling showProgress() on a not-attached fragment will throw a NPE
////                    // It is possible to ask isAdded() in the fragment, but it's better to ask in the presenter
////                    view?.showProgress(false)
////                    view?.showPokemon(pokemon)
////                    for (statistic in pokemon.stats) {
////                        view?.showStat(statistic)
////                    }
////                }) { throwable ->
////                    view?.showProgress(false)
////                    view?.showError(throwable)
////                }
////    }
//}
