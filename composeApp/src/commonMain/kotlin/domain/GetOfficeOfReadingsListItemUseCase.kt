package domain

import data.MainRepository
import kotlinx.datetime.LocalDate
import ui.main.ListItemHeaderUiState
import ui.main.ListItemType
import ui.main.ListItemUiState

class GetOfficeOfReadingsListItemUseCase(
    private val mainRepository: MainRepository,
) {

    private val baseItem = ListItemUiState(
        header = ListItemHeaderUiState(
            title = "Office of Readings",
        ),
        type = ListItemType.OFFICE_OF_READINGS,
        isEnabled = true,
        text = "",
        link = "",
    )

    suspend operator fun invoke(date: LocalDate): ListItemUiState {
        val office = mainRepository.retrieveCachedData(date)?.office
        return baseItem.copy(
            link = office?.officeOfReadings ?: "",
            text = office?.officeOfReadingsSubtitle
        )
    }
}