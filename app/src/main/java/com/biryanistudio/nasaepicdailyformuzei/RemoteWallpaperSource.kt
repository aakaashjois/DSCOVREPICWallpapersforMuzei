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


class RemoteWallpaperSource : RemoteMuzeiArtSource("RemoteWallpaperSource") {

    override fun onCreate() {
        super.onCreate()
        setUserCommands(MuzeiArtSource.BUILTIN_COMMAND_ID_NEXT_ARTWORK)
    }

    override fun onTryUpdate(reason: Int) {
        val sharedPreferences = getSharedPreferences(
                SettingsActivity.PREFERENCES_KEY, Context.MODE_PRIVATE)
        val imageMetadataUrl = "https://epic.gsfc.nasa.gov/api/natural"
        val connection = URL(imageMetadataUrl).openConnection() as HttpsURLConnection
        if (connection.responseCode == HttpsURLConnection.HTTP_OK) {
            val data = connection.inputStream.bufferedReader().readText()
            if (!data.isEmpty()) {
                val array = JSONArray(data)
                if (array.length() > 0) {
                    val imageData = array.getJSONObject(array.length() - 1)
                    if (imageData.has("date") && imageData.has("image")) {
                        val metaData = imageData.getString("date")
                        val date = SimpleDateFormat("yyyy/MM/dd")
                                .format(SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .parse(metaData))
                        val image = imageData.getString("image")
                        val imageUrl = "https://epic.gsfc.nasa.gov/archive/natural/$date/png/$image.png"
                        publishArtwork(Artwork.Builder()
                                .title("NASA EPIC Daily")
                                .byline("Captured at " + metaData)
                                .imageUri(Uri.parse(imageUrl))
                                .viewIntent(Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://epic.gsfc.nasa.gov/")))
                                .build())
                    } else throw RetryException()
                } else throw RetryException()
            } else throw RetryException()
        } else throw RetryException()
        connection.disconnect()
        val rotateTime = sharedPreferences.getInt(SettingsActivity.REFRESH_TIME, 1)
        if (rotateTime > 0) scheduleUpdate(System.currentTimeMillis() + rotateTime)
    }
}