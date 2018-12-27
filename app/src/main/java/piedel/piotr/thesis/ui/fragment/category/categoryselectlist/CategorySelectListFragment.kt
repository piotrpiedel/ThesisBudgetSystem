package piedel.piotr.thesis.ui.fragment.category.categoryselectlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.ui.base.BaseFragment
import javax.inject.Inject


class CategorySelectListFragment : BaseFragment(), CategorySelectListView, CategorySelectListAdapter.CategorySelectListAdapterOnClickHandler {


    lateinit var categorySelectListAdapter: CategorySelectListAdapter

    @Inject
    lateinit var categorySelectListPresenter: CategorySelectListPresenter

    @BindView(R.id.categories_list_view)
    lateinit var categoriesListView: RecyclerView

    override val layout: Int
        get() = R.layout.fragment_categories_list

    override val toolbarTitle: String
        get() = FRAGMENT_TITLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        categorySelectListPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutManager()
        initFragment()

    }

    private fun initFragment() {
        categorySelectListPresenter.loadParentCategories()
    }

    override fun onChildClick(childCategory: Category) {
        targetFragment?.onActivityResult(
                targetRequestCode,
                Activity.RESULT_OK,
                Intent().putExtra(FRAGMENT_INTENT_CATEGORY, childCategory)
        )
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    private fun setLayoutManager() {
        categoriesListView.layoutManager = LinearLayoutManager(context)
    }

    override fun updateList(listCategories: MutableList<CategoryExpandableGroup>) {
        categorySelectListAdapter = CategorySelectListAdapter(listCategories)
        categorySelectListAdapter.setOnChildClickListener(this)
        categoriesListView.adapter = categorySelectListAdapter
    }

    companion object {
        const val FRAGMENT_TAG = "categorySelectListFragment"
        const val FRAGMENT_TITLE = " Categories "
        const val FRAGMENT_INTENT_CATEGORY = "categoryValue"
    }
}