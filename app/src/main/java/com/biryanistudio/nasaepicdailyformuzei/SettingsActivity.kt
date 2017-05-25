package com.biryanistudio.nasaepicdailyformuzei

import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TimePicker
import org.jetbrains.anko.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        val PREFERENCES_KEY = "com.biryanistudio.nasaepicdailyformuzei.preferences"
        val ENHANCED_KEY = "enhanced"
        val REFRESH_HOUR = "refreshHour"
        val REFRESH_MINUTE = "refreshMinute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        var enhanceSwitch: Switch? = null
        var refreshTimePicker: TimePicker? = null
        scrollView {
            isFillViewport = true
            verticalLayout {
                orientation = LinearLayout.VERTICAL
                enhanceSwitch = switch {
                    padding = dip(16)
                    text = "Enhance Images"
                    textSize = 18f
                    setOnCheckedChangeListener { _, isChecked ->
                        val editor = sharedPreferences.edit()
                        editor.putBoolean(ENHANCED_KEY, isChecked)
                        editor.apply()
                    }
                }.lparams(width = matchParent, height = wrapContent)
                textView {
                    text = "Refresh time"
                    padding = dip(16)
                    textSize = 18f
                }.lparams(width = matchParent, height = wrapContent)
                refreshTimePicker = timePicker {
                    setOnTimeChangedListener { _, hourOfDay, minute ->
                        val editor = sharedPreferences.edit()
                        editor.putInt(REFRESH_HOUR, hourOfDay)
                        editor.putInt(REFRESH_MINUTE, minute)
                        editor.apply()
                    }
                    setIs24HourView(true)
                }.lparams(width = matchParent, height = wrapContent)
                textView {
                    padding = dip(16)
                    textSize = 18f
                    gravity = Gravity.CENTER
                    text = "The images provided by this app are published by the NASA EPIC Team"
                }.lparams(width = matchParent, height = wrapContent)
                textView {
                    padding = dip(16)
                    textSize = 18f
                    gravity = Gravity.CENTER
                    text = "This app has been completely designed using Kotlin!"
                }.lparams(width = matchParent, height = wrapContent)
                textView {
                    padding = dip(16)
                    textSize = 18f
                    gravity = Gravity.CENTER
                    text = "Made with \u2665 from Bengaluru, India"
                }.lparams(width = matchParent, height = wrapContent)

            }.lparams(width = matchParent, height = wrapContent)
        }
        enhanceSwitch?.isChecked = sharedPreferences.getBoolean(ENHANCED_KEY, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            refreshTimePicker?.hour = sharedPreferences.getInt(REFRESH_HOUR, 1)
            refreshTimePicker?.minute = sharedPreferences.getInt(REFRESH_MINUTE, 0)
        } else {
            refreshTimePicker?.currentHour = sharedPreferences.getInt(REFRESH_HOUR, 1)
            refreshTimePicker?.currentMinute = sharedPreferences.getInt(REFRESH_MINUTE, 0)
        }
    }
}
