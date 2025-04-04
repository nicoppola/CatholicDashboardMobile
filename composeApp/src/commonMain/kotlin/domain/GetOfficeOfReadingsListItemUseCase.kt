package domain

import data.MainRepository
import kotlinx.datetime.LocalDate
import ui.main.ListCollectionItemUiState
import ui.main.ListCollectionUiState
import ui.main.TextRow

class GetOfficeOfReadingsListItemUseCase(
    private val mainRepository: MainRepository,
) {

    suspend operator fun invoke(date: LocalDate): ListCollectionUiState {
        val office = mainRepository.retrieveCachedData(date)?.office
        return ListCollectionUiState(
            header = "Office of Readings",
            isExpanded = null,
            items = listOf(
                ListCollectionItemUiState(
                    link = office?.officeOfReadings ?: "",
                    rows = listOf(
                        TextRow(title = null, text = office?.officeOfReadingsSubtitle)
                    )
                )
            )
        )
    }
}