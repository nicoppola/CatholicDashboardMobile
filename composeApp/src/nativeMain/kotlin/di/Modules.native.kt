package di

import io.ktor.client.engine.darwin.Darwin
import networking.DefaultNovusClient
import networking.NovusClient
import networking.createHttpClient
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    single {
        DefaultNovusClient(createHttpClient(Darwin.create()))
    }.bind<NovusClient>()
}