package piedel.piotr.thesis.util.rxutils.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class NewThreadMainScheduler<T> () : BaseScheduler<T>(Schedulers.newThread(), AndroidSchedulers.mainThread())
