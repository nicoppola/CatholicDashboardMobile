package datastore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

@Composable
fun rememberSettingsRepo(
    prefs: DataStore<Preferences>
): SettingsRepository {
    return remember { SettingsRepository(prefs) }
}

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
) {

    //////////////////////// COLOR ////////////////////////
    suspend fun saveColorMode(colorMode: ColorMode) {
        dataStore.edit { prefs ->
            prefs[ColorMode.key] = colorMode.name
        }
    }

    suspend fun getColorMode(): Flow<ColorMode> {
        return dataStore.data.transform { data ->
            val pref = data.toPreferences()[ColorMode.key]
            val colorMode = pref?.let { ColorMode.valueOf(it) } ?: ColorMode.default
            emit(colorMode)
        }
    }

    //////////////////////// TIME ////////////////////////
    suspend fun saveTimeMode(timeMode: TimeMode) {
        dataStore.edit { prefs ->
            prefs[TimeMode.key] = timeMode.name
        }
    }

    suspend fun getTimeMode(): Flow<TimeMode> {
        return dataStore.data.transform { data ->
            val pref = data.toPreferences()[TimeMode.key]
            val timeMode = pref?.let { TimeMode.valueOf(it) } ?: TimeMode.default
            emit(timeMode)
        }
    }

}