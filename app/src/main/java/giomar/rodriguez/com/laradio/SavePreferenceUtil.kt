package giomar.rodriguez.com.laradio

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import giomar.rodriguez.com.laradio.model.Station

object SavePreferenceUtil {
    fun saveCurrentStation(context: Context, station: Station){
        val sharePref: SharedPreferences = context.getSharedPreferences("STATION", Context.MODE_PRIVATE)
        with(sharePref.edit()){
            val gson: Gson = Gson()
            val json = gson.toJson(station)
            putString("station", json)
            apply()
        }
    }
}