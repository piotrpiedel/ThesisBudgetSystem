package piedel.piotr.thesis.injection.module.ApplicationModule

import dagger.Module
import dagger.Provides
import piedel.piotr.thesis.data.remote.PokemonApi
import piedel.piotr.thesis.injection.scopes.ApplicationScope
import retrofit2.Retrofit

/**
 * Created by piotrpiedel
 */
@Module(includes = arrayOf(NetworkModule::class))
class ApiModule {

    @Provides
    @ApplicationScope
    internal fun providePokemonApi(retrofit: Retrofit): PokemonApi =
            retrofit.create(PokemonApi::class.java)
}