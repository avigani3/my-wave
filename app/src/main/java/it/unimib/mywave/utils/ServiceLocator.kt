package it.unimib.mywave.utils

import android.content.Context
import androidx.navigation.NavController
import it.unimib.mywave.model.AuthRepository
import it.unimib.mywave.model.ConfigurationRepository
import it.unimib.mywave.model.LocationRepository
import it.unimib.mywave.model.MapRepository
import it.unimib.mywave.model.MeteoRepository
import it.unimib.mywave.viewmodel.AuthViewModel
import it.unimib.mywave.viewmodel.AuthViewModelFactory

object ServiceLocator {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // AuthRepostory
    private var authRepository: AuthRepository? = null

    fun getAuthRepository(): AuthRepository {
        synchronized(this) {
            return authRepository ?: createAuthRepository()
        }
    }

    private fun createAuthRepository(): AuthRepository {
        val newRepo = AuthRepository()
        authRepository = newRepo
        return newRepo
    }

    // ConfigurationRepository
    private var configurationRepository: ConfigurationRepository? = null

    fun getConfigurationRepository(): ConfigurationRepository {
        synchronized(this) {
            return configurationRepository ?: createConfigurationRepository(appContext)
        }
    }

    private fun createConfigurationRepository(context: Context): ConfigurationRepository {
        val newRepo = ConfigurationRepository(context)
        configurationRepository = newRepo
        return newRepo
    }

    // LocationRepository
    private var locationRepository: LocationRepository? = null

    fun getLocationRepository(): LocationRepository {
        synchronized(this) {
            return locationRepository ?: createLocationRepository(appContext)
        }
    }

    private fun createLocationRepository(context: Context): LocationRepository {
        val newRepo = LocationRepository(context)
        locationRepository = newRepo
        return newRepo
    }

    // MapRepository
    private var mapRepository: MapRepository? = null

    fun getMapRepository(): MapRepository {
        synchronized(this) {
            return mapRepository ?: createMapRepository()
        }
    }

    private fun createMapRepository(): MapRepository {
        val newRepo = MapRepository()
        mapRepository = newRepo
        return newRepo
    }

    // MeteoRepository
    private var meteoRepository: MeteoRepository? = null

    fun getMeteoRepository(): MeteoRepository {
        synchronized(this) {
            return meteoRepository ?: createMeteoRepository()
        }
    }

    private fun createMeteoRepository(): MeteoRepository {
        val newRepo = MeteoRepository()
        meteoRepository = newRepo
        return newRepo
    }

    // AuthViewModel
    private var authViewModel: AuthViewModel? = null

    fun getAuthViewModel(navController: NavController): AuthViewModel {
        synchronized(this) {
            return authViewModel ?: createAuthViewModel(navController)
        }
    }

    private fun createAuthViewModel(navController: NavController): AuthViewModel {
        val newViewModel = AuthViewModelFactory(navController).create(AuthViewModel::class.java)
        authViewModel = newViewModel
        return newViewModel
    }




}