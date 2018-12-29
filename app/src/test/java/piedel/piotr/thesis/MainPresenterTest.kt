package piedel.thesis

import piedel.piotr.thesis.common.TestDataFactory
import piedel.piotr.thesis.data.AppDatabase
import piedel.piotr.thesis.ui.main.MainView
import piedel.piotr.thesis.ui.main.MainPresenter
import piedel.thesis.util.RxSchedulersOverrideRule
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by ravindra on 24/12/16.
 */
@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock lateinit var mMockMainView: MainView
    @Mock lateinit var mMockAppDatabase: AppDatabase
    private var mMainPresenter: MainPresenter? = null

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        mMainPresenter = MainPresenter(mMockAppDatabase)
        mMainPresenter?.attachView(mMockMainView)
    }

    @After
    fun tearDown() {
        mMainPresenter?.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun getPokemonReturnsPokemonNames() {
        val pokemonList = TestDataFactory.makePokemonNamesList(10)
        `when`(mMockAppDatabase.getPokemonList(10))
                .thenReturn(Single.just(pokemonList))

        mMainPresenter?.getPokemon(10)

        verify<MainView>(mMockMainView, times(2)).showProgressBar(anyBoolean())
        verify<MainView>(mMockMainView).showPokemon(pokemonList)
        verify<MainView>(mMockMainView, never()).showError(RuntimeException())

    }

    @Test
    @Throws(Exception::class)
    fun getPokemonReturnsError() {
        `when`(mMockAppDatabase.getPokemonList(10))
                .thenReturn(Single.error<List<String>>(RuntimeException()))

        mMainPresenter?.getPokemon(10)

        verify<MainView>(mMockMainView, times(2)).showProgressBar(anyBoolean())
//        verify<MainView>(mMockMainView).showError(RuntimeException())
        verify<MainView>(mMockMainView, never()).showPokemon(ArgumentMatchers.anyList<String>())
    }

}