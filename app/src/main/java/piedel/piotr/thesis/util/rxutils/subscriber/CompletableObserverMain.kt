package piedel.piotr.thesis.util.rxutils.subscriber

import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import timber.log.Timber

class CompletableObserverMain : CompletableObserver {
    override fun onComplete() {
        Timber.d(" CompletableObserverMain onSubscribe: finished with Succes")
    }

    override fun onSubscribe(d: Disposable) {
        Timber.d(" CompletableObserverMain  onSubscribe:  " +  d  + " Disposable : to String():  " + d.toString())

    }

    override fun onError(e: Throwable) {
        Timber.d(" CompletableObserverMain  onError:  " +  e  + " throwable : to String():  " + e.toString())
    }
}