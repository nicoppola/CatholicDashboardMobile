package domain

import data.MainRepository
import datastore.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import ui.main.ListItemHeaderUiState
import ui.main.ListItemType
import ui.main.ListItemUiState

class GetOfficeOfReadingsListItemUseCase(
    private val mainRepository: MainRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    private val baseItem = ListItemUiState(
        header = ListItemHeaderUiState(
            title = "Office of Readings",
        ),
        type = ListItemType.OFFICE_OF_READINGS,
        isEnabled = false,
        text = "",
        link = "",
    )

    operator fun invoke(date: LocalDate): Flow<ListItemUiState> {
        return preferencesRepository.getOfficeOfReadings().map { prefs ->
            val office = mainRepository.retrieveCachedData(date)?.office
            baseItem.copy(
                isEnabled = prefs.enabled,
                link = office?.officeOfReadings ?: "",
                text = office?.officeOfReadingsSubtitle
            )
        }
    }
}