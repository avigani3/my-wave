package it.unimib.mywave.model

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.unimib.mywave.data.Configurations
import it.unimib.mywave.utils.Utils
import it.unimib.mywave.utils.Utils.Companion.fromJsonToList
import it.unimib.mywave.utils.Utils.Companion.toJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ConfigurationRepository(private val context: Context) {
    private val userKey: String = Utils.getUserKey() ?: ""
    private val database =
        Firebase.database("https://fir-prova-7fe59-default-rtdb.europe-west1.firebasedatabase.app/")
    private val markersRef = database.reference.child("configurations")
    private val LIST_KEY = stringPreferencesKey("settings_key")

    suspend fun updateConfiguration(configKey: String, configuration: Configurations) {
        if (configuration.optionalData.isEmpty()) {
            deleteSettingsFromDataStore(context.dataStore)
            if (!Utils.isOffline(context)) {
                markersRef.child(configKey).removeValue().await()
            }
        } else {
            saveSettingsToDataStore(context.dataStore, configuration.optionalData)
            if (!Utils.isOffline(context)) {
                markersRef.child(configKey).child("optionalData")
                    .setValue(configuration.optionalData).await()
            }
        }
        Log.i("ConfigurationRepository", "Configuration updated successfully")
    }

    private suspend fun updateConfigurationFromLocal(configKey: String, configuration: Configurations) {

        if (!Utils.isOffline(context)) {
            markersRef.child(configKey).child("optionalData").setValue(configuration.optionalData)
                .await()
        }

        Log.i("ConfigurationRepository", "Configuration updated successfully")
    }

    suspend fun loadConfigurationsFromDatabase(): List<Configurations> {
        val storedSettings = getSettingsFromDataStore(context.dataStore)?.firstOrNull()
        var optionalConfig: List<Configurations>?
        if (!Utils.isOffline(context)) {
            if (storedSettings == null) {

                val dataSnapshot = markersRef.get().await()
                optionalConfig = dataSnapshot.children.mapNotNull { childSnapshot ->
                    val key = childSnapshot.key
                    val optionalData = childSnapshot.child("optionalData").value as? List<String>

                    if (key != null && childSnapshot.child("key").value == userKey && optionalData != null) {
                        Configurations(key, optionalData)
                    } else {
                        null
                    }
                }
                saveSettingsToDataStore(
                    context.dataStore,
                    optionalConfig.firstOrNull()?.optionalData ?: emptyList()
                )


            } else {
                optionalConfig = listOf(Configurations(userKey, storedSettings))
                updateConfigurationFromLocal(userKey, Configurations(userKey, storedSettings))
            }
        } else {
            optionalConfig = listOf(Configurations(userKey, storedSettings ?: emptyList()))
        }
        return optionalConfig
    }

    suspend fun getConfigurations(): Map<String, List<String>> {
        val optionalConfig = loadConfigurationsFromDatabase().firstOrNull()
        val configurations = mutableMapOf(
            "current" to mutableListOf("wave_height", "wave_direction"),
            "daily" to mutableListOf("wave_height_max", "wave_direction_dominant"),
            "hourly" to mutableListOf("wave_height", "wave_direction")
        )

        optionalConfig?.optionalData?.forEach { data ->
            configurations.forEach { (key, value) ->
                when (data) {
                    "wave_period" -> value.add(if (key == "daily") "wave_period_max" else "wave_period")
                    "wind_wave_height" -> value.add(if (key == "daily") "wind_wave_height_max" else "wind_wave_height")
                    "wind_wave_direction" -> value.add(if (key == "daily") "wind_wave_direction_dominant" else "wind_wave_direction")
                    "wind_wave_period" -> value.add(if (key == "daily") "wind_wave_period_max" else "wind_wave_period")
                    "wind_wave_peak_period" -> value.add(if (key == "daily") "wind_wave_peak_period_max" else "wind_wave_peak_period")
                    "swell_wave_height" -> value.add(if (key == "daily") "swell_wave_height_max" else "swell_wave_height")
                    "swell_wave_direction" -> value.add(if (key == "daily") "swell_wave_direction_dominant" else "swell_wave_direction")
                    "swell_wave_period" -> value.add(if (key == "daily") "swell_wave_period_max" else "swell_wave_period")
                    "swell_wave_peak_period" -> value.add(if (key == "daily") "swell_wave_peak_period_max" else "swell_wave_peak_period")
                }
            }
        }

        return configurations
    }

    private suspend fun saveSettingsToDataStore(
        dataStore: DataStore<Preferences>,
        list: List<String>
    ) {
        dataStore.edit { preferences ->
            preferences[LIST_KEY] = list.toJson()
        }
    }

    private suspend fun deleteSettingsFromDataStore(dataStore: DataStore<Preferences>) {
        dataStore.edit { preferences ->
            preferences[LIST_KEY] = ""
        }
    }

    private suspend fun getSettingsFromDataStore(dataStore: DataStore<Preferences>): Flow<List<String>>? {
        if (dataStore.data.firstOrNull() == null) {
            return null
        }
        return dataStore.data.map { preferences ->
            preferences[LIST_KEY]?.fromJsonToList() ?: emptyList()
        }
    }
}