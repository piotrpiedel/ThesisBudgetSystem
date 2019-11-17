package piedel.piotr.thesis

//
//import piedel.piotr.thesis.common.TestDataFactory
//import piedel.piotr.thesis.data.AppDatabase
//import piedel.thesis.util.RxSchedulersOverrideRule
//import io.reactivex.Single
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.ArgumentMatchers.anyInt
//import org.mockito.ArgumentMatchers.anyString
//import org.mockito.Mock
//import org.mockito.Mockito.`when`
//import org.mockito.junit.MockitoJUnitRunner
//
//@RunWith(MockitoJUnitRunner::class)
//class AppDatabaseTest {
//
//    @Rule @JvmField val mOverrideSchedulersRule = RxSchedulersOverrideRule()
//    @Mock lateinit var mMockPokemonApi: PokemonApi
//
//    private var mAppDatabase: AppDatabase? = null
//
//    @Before
//    fun setUp() {
//        mAppDatabase = AppDatabase(mMockPokemonApi)
//    }
//
//    @Test
//    fun getPokemonListCompletesAndEmitsPokemonList() {
//        val namedResourceList = TestDataFactory.makeNamedResourceList(5)
//        val pokemonListResponse = PokemonListResponse(namedResourceList)
//
//        `when`(mMockPokemonApi.getPokemonList(anyInt()))
//                .thenReturn(Single.just(pokemonListResponse))
//
//        mAppDatabase?.getPokemonList(10)
//                ?.test()
//                ?.assertComplete()
//                ?.assertValue(TestDataFactory.makePokemonNameList(namedResourceList))
//    }
//
//    @Test
//    fun getPokemonCompletesAndEmitsPokemon() {
//        val name = "charmander"
//        val pokemon = TestDataFactory.makePokemon(name)
//        `when`(mMockPokemonApi.getPokemon(anyString()))
//                .thenReturn(Single.just(pokemon))
//
//        mAppDatabase?.getPokemon(name)
//                ?.test()
//                ?.assertComplete()
//                ?.assertValue(pokemon)
//    }
//}
