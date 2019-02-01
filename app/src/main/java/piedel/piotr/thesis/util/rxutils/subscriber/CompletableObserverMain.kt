package piedel.piotr.thesis.util.rxutils.subscriber

import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import timber.log.Timber

open class CompletableObserverMain : CompletableObserver {

    override fun onComplete() {
        Timber.d(" CompletableObserverMain onComplete: finished with Success")
    }

    override fun onSubscribe(d: Disposable) {
        Timber.d(" CompletableObserverMain  onSubscribe: %s, Disposable : to String(): %s  ", d, d.toString())

    }

    override fun onError(e: Throwable) {
        Timber.d(e, " CompletableObserverMain  onError toString(): %s  ", e.toString())
    }
}