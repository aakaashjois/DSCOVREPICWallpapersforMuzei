package com.biryanistudio.nasaepicdailyformuzei

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.apps.muzei.api.Artwork
import com.google.android.apps.muzei.api.MuzeiArtSource
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource
import org.json.JSONArray
import java.net.URL
import java.text.SimpleDateFormat
import javax.net.ssl.HttpsURLConnection


class NasaEpicDailySource : RemoteMuzeiArtSource("NasaEpicDailySource") {

    override fun onCreate() {
        super.onCreate()
        setUserCommands(MuzeiArtSource.BUILTIN_COMMAND_ID_NEXT_ARTWORK)
    }

    override fun onTryUpdate(reason: Int) {
        val sharedPreferences = getSharedPreferences(
                SettingsActivity.PREFERENCES_KEY, Context.MODE_PRIVATE)
        val type = if (sharedPreferences.getBoolean(SettingsActivity.ENHANCED_KEY, false))
            "enhanced" else "natural"
        val rotateTime = (sharedPreferences.getInt(
                SettingsActivity.REFRESH_HOUR, 1) * 60 * 60 * 1000)
        +(sharedPreferences.getInt(SettingsActivity.REFRESH_MINUTE, 0) * 60 * 1000)
        val imageMetadataUrl = "https://epic.gsfc.nasa.gov/api/$type"
        val connection = URL(imageMetadataUrl).openConnection() as HttpsURLConnection
        try {
            if (connection.responseCode == HttpsURLConnection.HTTP_OK) {
                val data = connection.inputStream.bufferedReader().readText()
                if (!data.isEmpty()) {
                    val array = JSONArray(data)
                    val imageData = array.getJSONObject(array.length() - 1)
                    val token = imageData.getString("date")
                    val date = SimpleDateFormat("yyyy/MM/dd")
                            .format(SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .parse(token))
                    val image = imageData.getString("image")
                    val imageUrl = "https://epic.gsfc.nasa.gov/archive/$type/$date/png/$image.png"
                    publishArtwork(Artwork.Builder()
                            .title("NASA EPIC Daily")
                            .byline("Captured at " + token)
                            .imageUri(Uri.parse(imageUrl))
                            .viewIntent(Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://epic.gsfc.nasa.gov/")))
                            .build())
                }
            } else throw RetryException()
        } finally {
            connection.disconnect()
            scheduleUpdate(System.currentTimeMillis() + rotateTime)
        }
    }
}