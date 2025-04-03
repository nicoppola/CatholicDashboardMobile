package domain

import data.MainRepository
import kotlinx.datetime.LocalDate
import ui.main.ListItemHeaderUiState
import ui.main.ListItemType
import ui.main.ListItemUiState

class GetReadingsListItemUseCase(
    private val repository: MainRepository,
) {

    private val baseItem = ListItemUiState(
        type = ListItemType.READINGS,
        isEnabled = true,
        header = ListItemHeaderUiState("Daily Readings"),
    )

    suspend operator fun invoke(date: LocalDate): ListItemUiState {
        val readings = repository.retrieveCachedData(date)?.readings
        return baseItem.copy(
            text = "Reading 1: ${readings?.readingOne}\n" +
                    (if (readings?.readingTwo != null) "Reading 2: ${readings.readingTwo} \n" else "") +
                    "Psalm: ${readings?.psalm}\n" +
                    "Gospel: ${readings?.gospel}",
            link = readings?.link ?: ""
        )
    }
}