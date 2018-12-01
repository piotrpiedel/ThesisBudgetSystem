package piedel.piotr.thesis.util.rxutils.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ComputationMainScheduler<T> () : BaseScheduler<T>(Schedulers.computation(), AndroidSchedulers.mainThread())
