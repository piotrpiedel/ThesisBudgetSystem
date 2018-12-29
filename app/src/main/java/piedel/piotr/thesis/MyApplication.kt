package piedel.piotr.thesis

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.singhajit.sherlock.core.Sherlock
import com.squareup.leakcanary.LeakCanary
//import com.tspoon.traceur.Traceur
import io.fabric.sdk.android.Fabric
import piedel.piotr.thesis.injection.component.ApplicationComponent
import piedel.piotr.thesis.injection.component.DaggerApplicationComponent
import piedel.piotr.thesis.injection.module.applicationmodule.AppModule
import piedel.piotr.thesis.injection.module.applicationmodule.RoomModule
import timber.log.Timber
import com.akaita.java.rxjava2debug.RxJava2Debug


class MyApplication : MultiDexApplication() {

    internal var applicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            RxJava2Debug.enableRxJava2AssemblyTracking(arrayOf("com.piedel.piotr.thesis"))
//            Stetho.initializeWithDefaults(this)
            LeakCanary.install(this)
            Sherlock.init(this)
//            Traceur.enableLogging()
        }
    }

    // Needed to replace the component with a test specific one
    var component: ApplicationComponent
        get() {
            if (applicationComponent == null) {
                applicationComponent = DaggerApplicationComponent.builder()
                        .appModule(AppModule(this))
                        .roomModule(RoomModule(this))
                        .build()
            }
            return applicationComponent as ApplicationComponent
        }
        set(applicationComponent) {
            this.applicationComponent = applicationComponent
        }

    companion object {

        operator fun get(context: Context): MyApplication {
            return context.applicationContext as MyApplication
        }
    }
}
