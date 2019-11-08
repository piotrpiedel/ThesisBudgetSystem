package piedel.piotr.thesis.ui.activity.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import butterknife.BindView
import com.google.android.material.navigation.NavigationView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.ui.base.BaseActivity
import piedel.piotr.thesis.ui.fragment.category.categorylist.CategoryListFragment
import piedel.piotr.thesis.ui.fragment.chart.choosechart.ChooseChartFragment
import piedel.piotr.thesis.ui.fragment.importexport.importfromhtml.ImportExportFragment
import piedel.piotr.thesis.ui.fragment.ocr.googledrive.ImportFromImageDriveFragment
import piedel.piotr.thesis.ui.fragment.operation.operationlist.OperationListFragment
import piedel.piotr.thesis.ui.fragment.receipt.receiptlist.ReceiptListFragment
import piedel.piotr.thesis.util.showToast
import timber.log.Timber
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.MainView, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var mainPresenter: MainPresenter

    @BindView(R.id.main_activity_view_container)
    lateinit var drawerActivity: DrawerLayout

    @BindView(R.id.activity_navigation_view)
    lateinit var navigationView: NavigationView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.progress_bar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.fragment_container_activity_main)
    lateinit var fragmentContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        mainPresenter.attachView(this)
        setSupportActionBar(toolbar)
        setOpenSideBarListener()
        navigationView.setNavigationItemSelectedListener(this)
        mainPresenter.initStartingFragment()
    }

    fun getToolbarFromActivity(): Toolbar {
        return toolbar
    }

    override val layout: Int
        get() = R.layout.activity_main

    private fun setOpenSideBarListener() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(this,
                drawerActivity, toolbar,
                R.string.activity_drawer_layout_open,
                R.string.activity_drawer_layout_close)
        drawerActivity.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun initFirstFragment() {
        val operationsFragment = OperationListFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_activity_main, operationsFragment)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_operations -> {
                replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                        OperationListFragment(),
                        OperationListFragment.FRAGMENT_TAG
                )
            }
            R.id.navigation_categories -> {
                replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                        CategoryListFragment(),
                        CategoryListFragment.FRAGMENT_TAG)
            }
            R.id.navigation_import -> {
                replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                        ImportExportFragment(),
                        ImportExportFragment.FRAGMENT_TAG)
            }
            R.id.navigation_receipt_list -> {
                replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                        ReceiptListFragment(),
                        ReceiptListFragment.FRAGMENT_TAG)
            }
            R.id.navigation_chart -> {
                replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                        ChooseChartFragment(),
                        ChooseChartFragment.FRAGMENT_TAG)
            }
            R.id.navigation_drive_ocr -> {
                replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                        ImportFromImageDriveFragment(),
                        ImportFromImageDriveFragment.FRAGMENT_TAG)
            }
//            R.id.navigation_drive_temp -> {
//                replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
//                        OperationSelectableListFragment(),
//                        OperationSelectableListFragment.FRAGMENT_TAG)
//            }
        }
        drawerActivity.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showError(error: Throwable) {
        fragmentContainer.visibility = View.GONE
        showToast(baseContext, " There was an error while loading operations ")
        Timber.e(error, "There was an error while loading operations")
    }

    override fun onBackPressed() {
        if (drawerActivity.isDrawerOpen(GravityCompat.START)) {
            drawerActivity.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.detachView()
    }
}