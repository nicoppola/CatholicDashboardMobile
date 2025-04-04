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

    suspend operator fun invoke(date: LocalDate, isExpanded: Boolean = false): ListCollectionUiState {
        val readings = repository.retrieveCachedData(date)?.readings

        return ListCollectionUiState(
            isExpanded = isExpanded,
            header = "Daily Readings",
            items = readings?.map { readingSet ->
                ListCollectionItemUiState(
                    subHeader = readingSet.title,
                    rows = getVerseRows(readingSet),
                    link = readingSet.link,
                )
            } ?: emptyList()
        )
    }

    private fun getVerseRows(readings: CalendarData.Readings): List<TextRow> {
        return listOfNotNull(
            TextRow(title = "Reading 1", text = readings.readingOne),
            TextRow(title = "Psalm", text = readings.readingOne),
            readings.readingTwo?.let { TextRow(title = "Reading 2", text = it) },
            TextRow(title = "Gospel", text = readings.gospel)
        )
    }
}