package it.unimib.mywave.view.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import it.unimib.mywave.R
import it.unimib.mywave.data.Configurations
import it.unimib.mywave.view.components.CheckBoxComponent

@Composable
fun ConfigList (
    config: Configurations?,
    changeConfig: (String) -> Unit) {
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.wave_period),
        checked = config?.optionalData?.contains("wave_period") ?: false,
        onTextSelected = {
            changeConfig("wave_period")
        }, onCheckedChange = {
            changeConfig("wave_period")
        }
    )
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.wind_wave_height),
        checked = config?.optionalData?.contains("wind_wave_height")?: false,
        onTextSelected = {
            changeConfig("wind_wave_height")
        }, onCheckedChange = {
            changeConfig("wind_wave_height")
        }
    )
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.wind_wave_direction),
        checked = config?.optionalData?.contains("wind_wave_direction")?: false,
        onTextSelected = {
            changeConfig("wind_wave_direction")
        }, onCheckedChange = {
            changeConfig("wind_wave_direction")
        }
    )
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.wind_wave_period),
        checked = config?.optionalData?.contains("wind_wave_period")?: false,
        onTextSelected = {
            changeConfig("wind_wave_period")
        }, onCheckedChange = {
            changeConfig("wind_wave_period")
        }
    )
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.wind_wave_peak_period),
        checked = config?.optionalData?.contains("wind_wave_peak_period")?: false,
        onTextSelected = {
            changeConfig("wind_wave_peak_period")
        }, onCheckedChange = {
            changeConfig("wind_wave_peak_period")
        }
    )
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.swell_wave_height),
        checked = config?.optionalData?.contains("swell_wave_height")?: false,
        onTextSelected = {
            changeConfig("swell_wave_height")
        }, onCheckedChange = {
            changeConfig("swell_wave_height")
        }
    )
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.swell_wave_direction),
        checked = config?.optionalData?.contains("swell_wave_direction")?: false,
        onTextSelected = {
            changeConfig("swell_wave_direction")
        }, onCheckedChange = {
            changeConfig("swell_wave_direction")
        }
    )
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.swell_wave_period),
        checked = config?.optionalData?.contains("swell_wave_period")?: false,
        onTextSelected = {
            changeConfig("swell_wave_period")
        }, onCheckedChange = {
            changeConfig("swell_wave_period")
        }
    )
    CheckBoxComponent(
        labelValue = stringResource(id = R.string.swell_wave_peak_period),
        checked = config?.optionalData?.contains("swell_wave_peak_period")?: false,
        onTextSelected = {
            changeConfig("swell_wave_peak_period")
        }, onCheckedChange = {
            changeConfig("swell_wave_peak_period")
        }
    )
}