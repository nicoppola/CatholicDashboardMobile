package ui.theme

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

class MyDatePickerColors {
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun colors() = DatePickerDefaults.colors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
//          titleContentColor = ,
//          headlineContentColor = ,
//          weekdayContentColor = ,
//          subheadContentColor = ,
//          navigationContentColor = ,
//          yearContentColor = ,
//          disabledYearContentColor = ,
            currentYearContentColor = MaterialTheme.colorScheme.onPrimary,
            selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
//          disabledSelectedYearContentColor = ,
            selectedYearContainerColor = MaterialTheme.colorScheme.primaryContainer,
//          disabledSelectedYearContainerColor = ,
//          dayContentColor = ,
//          disabledDayContentColor = ,
//          selectedDayContentColor = ,
//          disabledSelectedDayContentColor = ,
            selectedDayContainerColor = MaterialTheme.colorScheme.primaryContainer,
//          disabledSelectedDayContainerColor = ,
            todayContentColor = MaterialTheme.colorScheme.onPrimary,
            todayDateBorderColor = MaterialTheme.colorScheme.onPrimary,
//          dayInSelectionRangeContainerColor = ,
//          dayInSelectionRangeContentColor = ,
//            dividerColor =,
//            dateTextFieldColors =,
        )

    }
}