package piedel.piotr.thesis.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.util.LongSparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import piedel.piotr.thesis.MyApplication
import piedel.piotr.thesis.injection.component.ConfigPersistentComponent
import piedel.piotr.thesis.injection.component.DaggerConfigPersistentComponent
import piedel.piotr.thesis.injection.component.FragmentComponent
import piedel.piotr.thesis.injection.module.FragmentModule
import piedel.piotr.thesis.ui.main.MainActivity
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong

/**
 * Abstract Fragment that every other Fragment in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent are kept
 * across configuration changes.
 */
abstract class BaseFragment : Fragment() {

    private var fragmentComponent: FragmentComponent? = null
    private var fragmentId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the FragmentComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        fragmentId = savedInstanceState?.getLong(KEY_FRAGMENT_ID) ?: NEXT_ID.getAndIncrement()

        val configPersistentComponent: ConfigPersistentComponent

        if (sComponentsArray.get(fragmentId) == null) {

            Timber.i("Creating new ConfigPersistentComponent id=%d", fragmentId)

            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(MyApplication[requireContext()].component)
                    .build()

            sComponentsArray.put(fragmentId, configPersistentComponent)

        } else {

            Timber.i("Reusing ConfigPersistentComponent id=%d", fragmentId)

            configPersistentComponent = sComponentsArray.get(fragmentId) as ConfigPersistentComponent
        }

        fragmentComponent = configPersistentComponent.fragmentComponent(FragmentModule(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater.inflate(layout, container, false)
        ButterKnife.bind(this, view as View)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        val toolbar = (activity as MainActivity).getToolbarFromActivity()
        toolbar.title = toolbarTitle
    }

    abstract val layout: Int

    abstract val toolbarTitle: String

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        outState.putLong(KEY_FRAGMENT_ID, fragmentId)

    }

    override fun onDestroy() {
        if (!requireActivity().isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", fragmentId)
            sComponentsArray.remove(fragmentId)
        }
        super.onDestroy()
    }

    fun getFragmentComponent(): FragmentComponent {
        return fragmentComponent as FragmentComponent
    }

    fun getBaseActivity(): BaseActivity {
        return requireActivity() as BaseActivity
    }

    companion object {

        private val KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID"
        private val sComponentsArray = LongSparseArray<ConfigPersistentComponent>()
        private val NEXT_ID = AtomicLong(0)
    }
}
