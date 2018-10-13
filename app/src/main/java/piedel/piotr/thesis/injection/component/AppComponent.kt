package piedel.piotr.thesis.injection.component

import android.app.Application
import android.content.Context
import dagger.Component
import piedel.piotr.thesis.data.DataManager
import piedel.piotr.thesis.data.remote.PokemonApi
import piedel.piotr.thesis.injection.ApplicationContext
import piedel.piotr.thesis.injection.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun dataManager(): DataManager

    fun pokemonApi(): PokemonApi
}
