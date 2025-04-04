package di

//import datastore.getDataStore
import datastore.SettingsRepository
import datastore.createDataStore
import io.ktor.client.engine.okhttp.OkHttp
import networking.DefaultMyClient
import networking.MyClient
import networking.createHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single {
        DefaultMyClient(createHttpClient(OkHttp.create()))
    }.bind<MyClient>()

    single {
        createDataStore(androidContext())
    }

    single {
        SettingsRepository(get())
    }
}