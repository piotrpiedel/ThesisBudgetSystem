package piedel.piotr.thesis.ui.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

//import rx.Subscription
//import rx.subscriptions.CompositeSubscription

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the view that
 * can be accessed from the children classes by calling getView().
 */
open class BasePresenter<T : BaseView> : Presenter<T> {

    var view: T? = null
        private set

    private val compositeDisposable = CompositeDisposable()

    override fun attachView(mvpView: T) {
        this.view = mvpView
    }

    override fun detachView() {
        view = null
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    val isViewAttached: Boolean
        get() = view != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

//    fun addSubscription(subscription: Subscription?) {
//        compositeSubscription.add(subscription)
//    }

    fun addDisposable(disposable: Disposable?) {
        disposable?.let { compositeDisposable.add(it) }
    }

    private class MvpViewNotAttachedException internal constructor() : RuntimeException("Please call Presenter.attachView(BaseView) before" + " requesting data to the Presenter")

}

