package piedel.piotr.thesis.util.rxutils.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SingleMainScheduler<T> : BaseScheduler<T>(Schedulers.single(), AndroidSchedulers.mainThread())
