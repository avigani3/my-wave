package it.unimib.mywave.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Utils {

    companion object {
        fun formatDate(date: String): String {
            val year = date.substring(0, 4)
            val month = date.substring(5, 7)
            val day = date.substring(8, 10)
            return "$day/$month/$year"
        }

        fun getUserKey() : String? {
            var key: String? = null
            FirebaseAuth.getInstance().currentUser?.also {
                key = it.uid
            }
            return key
        }

        fun List<String>.toJson(): String {
            return Gson().toJson(this)
        }

        fun String.fromJsonToList(): List<String> {
            if (this.isEmpty()) return listOf()
            val listType: Type = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson(this, listType)
        }

        fun isOffline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return networkInfo == null || !(networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        }


    }
}