package datastore

import androidx.datastore.preferences.core.stringPreferencesKey

enum class ColorMode{
    DARK,
    LIGHT,
    SYSTEM;

    companion object {
        val default = SYSTEM
        val key = stringPreferencesKey("COLOR_MODE_PREF")
    }
}

enum class TimeMode{
    HOUR_24,
    HOUR_12,
    SYSTEM;

    companion object{
        val default = SYSTEM
        val key = stringPreferencesKey("TIME_MODE_PREF")
    }
}

