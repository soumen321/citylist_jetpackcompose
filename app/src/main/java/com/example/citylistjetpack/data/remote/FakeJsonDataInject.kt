package com.example.citylistjetpack.data.remote

import android.content.Context
import com.example.citylistjetpack.domain.model.CityList
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

object FakeJsonDataInject {

    private var cityList = mutableListOf<CityList>()

    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        var json: String? = null
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun injectData(jsonString:String) {
        try {
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("items")
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.optJSONObject(i)
                // Parse individual items from JSON
                val city = CityList(
                    city = item.optString("city"),
                    capital = item.optString("admin_name"),
                    population = item.optString("population"),
                    lat = item.optString("lat"),
                    lng = item.optString("lng")
                )
                cityList.add(city)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun getCitListFromApi(): List<CityList> {
        return cityList
    }
}