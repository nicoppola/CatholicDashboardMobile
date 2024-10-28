package datastore

import android.content.Context
import androidx.datastore.core.DataStore
import org.coppola.catholic.PreferenceData
import org.coppola.catholic.AndroidPlatformContextProvider
import okio.FileSystem
import okio.Path.Companion.toPath

fun getDataStore(context: Context): DataStore<PreferenceData> {
    val content = requireNotNull(context)
    val producePath = { content.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath.toPath() }

    return createDataStore(fileSystem = FileSystem.SYSTEM, producePath = producePath)
}