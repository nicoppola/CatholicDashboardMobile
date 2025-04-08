package domain

import data.CalendarData
import data.MainRepository
import kotlinx.datetime.LocalDate
import ui.main.ListCollectionItemUiState
import ui.main.ListCollectionUiState
import ui.main.TextRow

class GetReadingsListItemUseCase(
    private val repository: MainRepository,
) {

    suspend operator fun invoke(date: LocalDate): ListCollectionUiState {
        val readings = repository.retrieveCachedData(date)?.readings
        val willBeExpanded = readings?.let { if(it.size > 1) false else null }

        return ListCollectionUiState(
            isExpanded = willBeExpanded,
            header = "Daily Readings",
            items = readings?.mapIndexed { index, readingSet ->
                ListCollectionItemUiState(
                    subHeader = willBeExpanded?.let { readingSet.title },
                    rows = getVerseRows(readingSet),
                    link = readingSet.link,
                    showOnCollapsed = index == 0
                )
            } ?: emptyList()
        )
    }

    private fun getVerseRows(readings: CalendarData.Readings): List<TextRow> {
        return listOfNotNull(
            TextRow(title = "Reading 1: ", text = readings.readingOne),
            TextRow(title = "Psalm: ", text = readings.psalm),
            readings.readingTwo?.let { TextRow(title = "Reading 2: ", text = it) },
            TextRow(title = "Gospel: ", text = readings.gospel)
        )
    }
}