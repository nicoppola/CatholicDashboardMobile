package di

import datastore.SettingsRepository
import datastore.createDataStore
import io.ktor.client.engine.darwin.Darwin
import networking.DefaultMyClient
import networking.MyClient
import networking.createHttpClient
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single {
        DefaultMyClient(createHttpClient(Darwin.create()))
    }.bind<MyClient>()

    single {
        createDataStore()
    }

    single {
        SettingsRepository(get())
    }.bind<SettingsRepository>()
}