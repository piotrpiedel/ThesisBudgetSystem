package piedel.piotr.thesis.ui.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import butterknife.BindView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.ui.base.BaseActivity
import piedel.piotr.thesis.ui.fragment.category.categorylist.CategoryFragment
import piedel.piotr.thesis.ui.fragment.importexport.ImportExportFragment
import piedel.piotr.thesis.ui.fragment.operation.operationlist.OperationFragment
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var mainPresenter: MainPresenter

    @BindView(R.id.main_activity_view_container)
    lateinit var drawerActivity: DrawerLayout

    @BindView(R.id.activity_navigation_view)
    lateinit var navigationView: NavigationView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

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
        val operationsFragment = OperationFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_activity_main, operationsFragment)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_operations -> {
                fragmentReplaceWithBackStack(R.id.fragment_container_activity_main,
                        OperationFragment(),
                        OperationFragment.FRAGMENT_TAG
                )
            }
            R.id.navigation_categories -> {
                fragmentReplaceWithBackStack(R.id.fragment_container_activity_main,
                        CategoryFragment(),
                        CategoryFragment.FRAGMENT_TAG)
            }
            R.id.navigation_import -> {
                fragmentReplaceWithBackStack(R.id.fragment_container_activity_main,
                        ImportExportFragment(),
                        ImportExportFragment.FRAGMENT_TAG)
            }
        }
        drawerActivity.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showProgress(show: Boolean) {
    }

    override fun showError(error: Throwable) {
        fragmentContainer.visibility = View.GONE
        Toast.makeText(baseContext, "There was an error while loading operations", Toast.LENGTH_SHORT).show()
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