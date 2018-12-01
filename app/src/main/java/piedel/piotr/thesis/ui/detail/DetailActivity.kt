//package piedel.piotr.thesis.ui.detail
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.support.v7.widget.Toolbar
//import android.view.View
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.ProgressBar
//import butterknife.BindView
//import piedel.piotr.thesis.R
//import piedel.piotr.thesis.ui.base.BaseActivity
//import piedel.piotr.thesis.ui.common.ErrorView
//import piedel.piotr.thesis.ui.detail.widget.StatisticView
//import piedel.piotr.thesis.util.loadImageFromUrl
//import timber.log.Timber
//import javax.inject.Inject
//
//class DetailActivity : BaseActivity(), DetailView, ErrorView.ErrorListener {
//
//    @Inject
//    lateinit var mDetailPresenter: DetailPresenter
//
//    @BindView(R.id.view_error)
//    @JvmField
//    var errorView: ErrorView? = null
//    @BindView(R.id.image_pokemon)
//    @JvmField
//    var mPokemonImage: ImageView? = null
//    @BindView(R.id.progress)
//    @JvmField
//    var progress: ProgressBar? = null
//    @BindView(R.id.toolbar)
//    @JvmField
//    var toolbar: Toolbar? = null
//    @BindView(R.id.layout_stats)
//    @JvmField
//    var mStatLayout: LinearLayout? = null
//    @BindView(R.id.layout_pokemon)
//    @JvmField
//    var mPokemonLayout: View? = null
//
//    private var mPokemonName: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        getActivityComponent().inject(this)
//        mDetailPresenter.attachView(this)
//
//        mPokemonName = intent.getStringExtra(EXTRA_POKEMON_NAME)
//        if (mPokemonName == null) {
//            throw IllegalArgumentException("Detail Activity requires a pokemon name@")
//        }
//
//        setSupportActionBar(toolbar)
//        val actionBar = supportActionBar
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//        title = mPokemonName?.substring(0, 1)?.toUpperCase() + mPokemonName?.substring(1)
//
//        errorView?.setErrorListener(this)
//
////        mDetailPresenter.getPokemon(mPokemonName as String)
//    }
//
//    override val layout: Int
//        get() = R.layout.activity_detail
//
//    override fun showPokemon(pokemon: Pokemon) {
//        if (pokemon.sprites.frontDefault != null) {
//            mPokemonImage?.loadImageFromUrl(pokemon.sprites.frontDefault as String)
//        }
//        mPokemonLayout?.visibility = View.VISIBLE
//    }
//
//    override fun showStat(statistic: Statistic) {
//        val statisticView = StatisticView(this)
//        statisticView.setStat(statistic)
//        mStatLayout?.addView(statisticView)
//    }
//
//    override fun showProgress(show: Boolean) {
//        errorView?.visibility = View.GONE
//        progress?.visibility = if (show) View.VISIBLE else View.GONE
//    }
//
//    override fun showError(error: Throwable) {
//        mPokemonLayout?.visibility = View.GONE
//        errorView?.visibility = View.VISIBLE
//        Timber.e(error, "There was a problem retrieving the pokemon...")
//    }
//
//    override fun onReloadData() {
////        mDetailPresenter.getPokemon(mPokemonName as String)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mDetailPresenter.detachView()
//    }
//
//    companion object {
//
//        val EXTRA_POKEMON_NAME = "EXTRA_POKEMON_NAME"
//
//        fun getStartIntent(context: Context, pokemonName: String): Intent {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra(EXTRA_POKEMON_NAME, pokemonName)
//            return intent
//        }
//    }
//}