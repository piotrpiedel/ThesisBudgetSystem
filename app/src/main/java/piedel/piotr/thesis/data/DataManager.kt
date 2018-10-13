package piedel.piotr.thesis.data

import piedel.piotr.thesis.data.model.Pokemon
import piedel.piotr.thesis.data.remote.PokemonApi
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject
constructor(private val mPokemonApi: PokemonApi) {

    fun getPokemonList(limit: Int): Single<List<String>> {
        return mPokemonApi.getPokemonList(limit)
                .toObservable()
                .flatMapIterable { (results) -> results }
                .map { (name) -> name }
                .toList()
    }

    fun getPokemon(name: String): Single<Pokemon> {
        return mPokemonApi.getPokemon(name)
    }

}