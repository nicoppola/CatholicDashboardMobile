package di

import data.V2MainRepository
import domain.GetTodayIconUseCase
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import ui.main.MainViewModel
import ui.settings.SettingsViewModel


expect val platformModule: Module

val sharedModule = module {
    single { V2MainRepository(get(),) }
    single { GetTodayIconUseCase() }
    viewModel { MainViewModel(get(), get(),)}
    viewModel { SettingsViewModel()}
}