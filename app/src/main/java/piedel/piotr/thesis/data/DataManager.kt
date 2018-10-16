package piedel.piotr.thesis.data

import piedel.piotr.thesis.data.model.Pokemon
import piedel.piotr.thesis.data.remote.PokemonApi
import io.reactivex.Single
import piedel.piotr.thesis.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class DataManager @Inject
constructor(private val pokemonApi: PokemonApi) {

    fun getPokemonList(limit: Int): Single<List<String>> {
        return pokemonApi.getPokemonList(limit)
                .toObservable()
                .flatMapIterable { (results) -> results }
                .map { (name) -> name }
                .toList()
    }

    fun getPokemon(name: String): Single<Pokemon> {
        return pokemonApi.getPokemon(name)
    }

}