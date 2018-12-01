package piedel.piotr.thesis.util.rxutils.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrampolineMainScheduler<T> () : BaseScheduler<T>(Schedulers.trampoline(), AndroidSchedulers.mainThread())
