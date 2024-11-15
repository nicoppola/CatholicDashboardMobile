package di

import data.DefaultMainRepository
import data.MainRepository
import domain.GetOfficeListItemUseCase
import domain.GetOfficeOfReadingsListItemUseCase
import domain.GetReadingsListItemUseCase
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.main.MainViewModel
import ui.settings.SettingsViewModel


expect val platformModule: Module

val sharedModule = module {
    singleOf(::DefaultMainRepository).bind<MainRepository>()
    single { GetOfficeListItemUseCase(get(), get()) }
    single { GetReadingsListItemUseCase(get(), get()) }
    single { GetOfficeOfReadingsListItemUseCase(get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get())}
    viewModel { SettingsViewModel(get())}
}