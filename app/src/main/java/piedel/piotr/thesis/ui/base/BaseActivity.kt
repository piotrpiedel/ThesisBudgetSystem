package piedel.piotr.thesis.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.LongSparseArray
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import piedel.piotr.thesis.MyApplication
import piedel.piotr.thesis.injection.component.ActivityComponent
import piedel.piotr.thesis.injection.component.ConfigPersistentComponent
import piedel.piotr.thesis.injection.component.DaggerConfigPersistentComponent
import piedel.piotr.thesis.injection.module.ActivityModule
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong

/**
 * Abstract activity that every other Activity in this application must implement. It provides the
 * following functionality:
 * - Handles creation of Dagger components and makes sure that instances of
 * ConfigPersistentComponent are kept across configuration changes.
 * - Set up and handles a GoogleApiClient instance that can be used to access the Google sign in
 * api.
 * - Handles signing out when an authentication error event is received.
 */
abstract class BaseActivity : AppCompatActivity() {

    private var activityComponent: ActivityComponent? = null
    private var activityId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        ButterKnife.bind(this)
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()
        val configPersistentComponent: ConfigPersistentComponent
        if (componentsArray.get(activityId) == null) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", activityId)
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(MyApplication[this].component)
                    .build()
            componentsArray.put(activityId, configPersistentComponent)
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", activityId)
            configPersistentComponent = componentsArray.get(activityId) as ConfigPersistentComponent
        }
        activityComponent = configPersistentComponent
                .activityComponent(ActivityModule(this))
        activityComponent?.inject(this)
    }

    abstract val layout: Int

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, activityId)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", activityId)
            componentsArray.remove(activityId)
        }
        super.onDestroy()
    }

    fun getActivityComponent(): ActivityComponent {
        return activityComponent as ActivityComponent
    }

    fun addFragmentWithoutBackStack(layoutResId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
                .add(layoutResId, fragment, tag)
                .commit()
    }

    fun addFragmentWithBackStack(layoutResId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
                .add(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    fun replaceFragmentWithBackStack(layoutResId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
                .replace(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    fun replaceFragmentWithoutBackStack(layoutResId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
                .replace(layoutResId, fragment, tag)
                .commit()
    }

    companion object {

        private val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        private val NEXT_ID = AtomicLong(0)
        private val componentsArray = LongSparseArray<ConfigPersistentComponent>()
    }

}
