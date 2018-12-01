package piedel.thesis

import piedel.piotr.thesis.common.TestDataFactory
import piedel.piotr.thesis.data.AppDatabase
import piedel.piotr.thesis.ui.detail.DetailView
import piedel.piotr.thesis.ui.detail.DetailPresenter
import piedel.thesis.util.RxSchedulersOverrideRule
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by ravindra on 24/12/16.
 */
@RunWith(MockitoJUnitRunner::class)
class DetailPresenterTest {

    @Mock lateinit var mMockDetailView: DetailView
    @Mock lateinit var mMockAppDatabase: AppDatabase
    private var mDetailPresenter: DetailPresenter? = null

    @JvmField
    @Rule val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mDetailPresenter = DetailPresenter(mMockAppDatabase)
        mDetailPresenter?.attachView(mMockDetailView)
    }

    @After
    fun tearDown() {
        mDetailPresenter?.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun getPokemonDetailReturnsPokemon() {
        val pokemon = TestDataFactory.makePokemon("id")
        `when`(mMockAppDatabase.getPokemon(anyString()))
                .thenReturn(Single.just(pokemon))

        mDetailPresenter?.getPokemon(anyString())

        verify<DetailView>(mMockDetailView, times(2)).showProgress(anyBoolean())
        verify<DetailView>(mMockDetailView).showPokemon(pokemon)
        verify<DetailView>(mMockDetailView, never()).showError(RuntimeException())
    }

    @Test
    @Throws(Exception::class)
    fun getPokemonDetailReturnsError() {
        `when`(mMockAppDatabase.getPokemon("id"))
                .thenReturn(Single.error<Pokemon>(RuntimeException()))

        mDetailPresenter?.getPokemon("id")

        verify<DetailView>(mMockDetailView, times(2)).showProgress(anyBoolean())
//        verify<DetailView>(mMockDetailView).showError(any(Throwable::class.java))
//        verify<DetailView>(mMockDetailView, never()).showPokemon(any(Pokemon::class.java))
    }

}