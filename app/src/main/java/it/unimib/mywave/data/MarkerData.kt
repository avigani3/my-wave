package it.unimib.mywave.data

data class MarkerData(val key: String, val name: String = "Spot", val latitude: Double, val longitude: Double, val type: Int, val userId: String)