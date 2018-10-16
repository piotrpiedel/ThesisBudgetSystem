package piedel.piotr.thesis.ui.base

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the BaseView type that wants to be attached with.
 */
interface Presenter<V : BaseView> {

    fun attachView(mvpView: V)

    fun detachView()
}
