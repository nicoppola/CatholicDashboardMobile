package di

import data.DefaultMainRepository
import data.MainRepository
import datastore.PreferencesRepository
import domain.GetOfficeListItemUseCase
import org.koin.compose.viewmodel.dsl.viewModel
import ui.main.MainViewModel
import ui.settings.SettingsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


expect val platformModule: Module

val sharedModule = module {
    singleOf(::DefaultMainRepository).bind<MainRepository>()
    single { GetOfficeListItemUseCase(get()) }
    viewModel { MainViewModel(get(), get())}
    viewModelOf(::SettingsViewModel)
}