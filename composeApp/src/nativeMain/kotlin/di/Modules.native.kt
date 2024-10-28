//package di
//
//import datastore.PreferencesRepository
//import domain.GetOfficeListItemUseCase
//import io.ktor.client.engine.darwin.Darwin
//import networking.DefaultMyClient
//import networking.MyClient
//import networking.createHttpClient
//import org.koin.dsl.bind
//import org.koin.dsl.module
//
//actual val platformModule = module {
//
//    single {
//        DefaultMyClient(createHttpClient(Darwin.create()))
//    }.bind<MyClient>()
//
//    single {
//        getDataStore()
//    }
//
//    single {
//        PreferencesRepository(get())
//    }.bind<PreferencesRepository>()
//
//    single {
//        GetOfficeListItemUseCase(get())
//    }
//}