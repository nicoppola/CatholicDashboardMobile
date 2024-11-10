package datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.coppola.catholic.OfficeOfReadingsPrefs
import org.coppola.catholic.OfficePrefs
import org.coppola.catholic.PreferenceData
import org.coppola.catholic.ReadingsPrefs

// https://medium.com/@aribmomin111/unlocking-proto-datastore-magic-in-kmm-d397f40a0805

class PreferencesRepository(private val dataStore: DataStore<PreferenceData>) {

    ////////////// Readings //////////////
     suspend fun updateReadings(newSettings: ReadingsPrefs) {
        dataStore.updateData { data ->
            data.copy(readings = newSettings)
        }
    }

     fun getReadings(): Flow<ReadingsPrefs> {
        return dataStore.data.map { data ->
            data.readings ?: PreferenceDefaults.readings
        }
    }

    ////////////// Office //////////////
    suspend fun updateOffice(newSettings: OfficePrefs) {
        dataStore.updateData { data ->
            data.copy(office = newSettings)
        }
    }

    fun getOffice(): Flow<OfficePrefs> {
        println("******* IN GET OFFICE!!!")
        return dataStore.data.map { data ->
            data.office ?: PreferenceDefaults.office
        }
    }

    ////////////// Office of Readings //////////////
    suspend fun updateOfficeOfReadings(newSettings: OfficeOfReadingsPrefs) {
        dataStore.updateData { data ->
            data.copy(officeOfReadings = newSettings)
        }
    }

    fun getOfficeOfReadings(): Flow<OfficeOfReadingsPrefs> {
        return dataStore.data.map { data ->
            data.officeOfReadings ?: PreferenceDefaults.officeOfReadings
        }
    }
}