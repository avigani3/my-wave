package it.unimib.mywave.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.unimib.mywave.data.Configurations
import it.unimib.mywave.model.ConfigurationRepository
import it.unimib.mywave.utils.ServiceLocator
import it.unimib.mywave.utils.Utils

class ConfigurationsViewModel(
    private val userKey: String = Utils.getUserKey() ?: "",
    private val repository: ConfigurationRepository = ServiceLocator.getConfigurationRepository()
) : ViewModel() {

    private val _optionalConfig = MutableLiveData<Configurations>()
    val optionalConfig: LiveData<Configurations> = _optionalConfig

    private val _newConfig = MutableLiveData<Configurations>()
    private val newConfig: LiveData<Configurations> = _newConfig

    private suspend fun updateConfiguration() {
        repository.updateConfiguration(newConfig.value!!.key, newConfig.value!!)
        _optionalConfig.value = newConfig.value
    }

    suspend fun getConfigurationsForProfile() {
        if (_optionalConfig.value?.optionalData.isNullOrEmpty()) {
            val dbConfigurations = repository.loadConfigurationsFromDatabase()
            if (dbConfigurations.isNotEmpty()) {
                _optionalConfig.value = dbConfigurations.first()
            }
            if (_newConfig.isInitialized.not()) {
                _newConfig.value = _optionalConfig.value ?: Configurations(userKey, ArrayList())
            }
        }

    }

    suspend fun changeConfig(x: String) {
        if (newConfig.value!!.optionalData.contains(x)) {
            _newConfig.value = Configurations(userKey, _newConfig.value!!.optionalData.minus(x))
            updateConfiguration()
        } else {
            _newConfig.value = Configurations(userKey, _newConfig.value!!.optionalData.plus(x))
            updateConfiguration()
        }
    }
}
