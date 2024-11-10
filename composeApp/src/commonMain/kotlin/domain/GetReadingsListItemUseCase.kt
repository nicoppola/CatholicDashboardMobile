package domain

import data.CalendarData
import datastore.PreferencesRepository
import kotlinx.coroutines.flow.first
import ui.main.ListItemUiState

class GetReadingsListItemUseCase(
    private val preferencesRepository: PreferencesRepository,
) {

    suspend operator fun invoke(readings: CalendarData.Readings?): ListItemUiState? {
        return if (readings != null && preferencesRepository.getReadings().first().enabled) {
            ListItemUiState(
                title = "Daily Readings",
                text = "Reading 1: ${readings.readingOne}\n" +
                        (if (readings.readingTwo != null) "Reading 2: $readings.readingTwo \n" else "") +
                        "Psalm: ${readings.psalm}\n" +
                        "Gospel: ${readings.gospel}",
                link = readings.link,
            )
        } else {
            null
        }
    }

}