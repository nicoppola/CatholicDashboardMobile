package domain

import data.CalendarData
import datastore.PreferencesRepository
import kotlinx.coroutines.flow.first
import ui.main.ListItemUiState

class GetOfficeOfReadingsListItemUseCase(
    private val preferencesRepository: PreferencesRepository,
) {

    suspend operator fun invoke(office: CalendarData.Office?): ListItemUiState? {
        return if (office != null && preferencesRepository.getOfficeOfReadings().first().enabled) {
            ListItemUiState(
                title = "Office of Readings",
                text = "Office of Readings",
                link = office.officeOfReadings,
            )
        } else {
            null
        }
    }

}