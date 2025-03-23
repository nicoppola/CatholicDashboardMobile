package domain

import data.MainRepository
import datastore.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import ui.main.ListItemHeaderUiState
import ui.main.ListItemType
import ui.main.ListItemUiState

class GetReadingsListItemUseCase(
    private val repository: MainRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    private val baseItem = ListItemUiState(
        type = ListItemType.READINGS,
        isEnabled = false,
        header = ListItemHeaderUiState("Daily Readings"),
    )

    suspend operator fun invoke(date: LocalDate): Flow<ListItemUiState> {
        return preferencesRepository.getReadings().map {
            val readings = repository.retrieveCachedData(date)?.readings
            baseItem.copy(
                isEnabled = it.enabled,
                text = "Reading 1: ${readings?.readingOne}\n" +
                        (if (readings?.readingTwo != null) "Reading 2: ${readings.readingTwo} \n" else "") +
                        "Psalm: ${readings?.psalm}\n" +
                        "Gospel: ${readings?.gospel}",
                link = readings?.link ?: ""
            )
        }
    }
}