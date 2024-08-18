package domain

import data.MainRepository
import ui.MainUiState

class RetrieveMainDataUseCase(mainRepo: MainRepository) {
    // todo - decide which calendar to use

    operator fun invoke(): MainUiState {
        return MainUiState()
    }
}