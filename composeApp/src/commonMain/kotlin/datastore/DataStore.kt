package datastore

//import androidx.datastore.core.DataStore
//import androidx.datastore.core.DataStoreFactory
//import androidx.datastore.core.okio.OkioStorage
//import org.coppola.catholic.PreferenceData
//import okio.FileSystem
//import okio.Path


//internal const val DATA_STORE_FILE_NAME = "proto_datastore.preferences_pb"
//
//fun createDataStore(producePath: () -> String): DataStore<PreferenceData> =
//    PreferenceDataStore.create(
//        storage = OkioStorage(
//            fileSystem = fileSystem,
//            producePath = producePath,
//            serializer = PreferenceSerializer,
//        ),
//    )