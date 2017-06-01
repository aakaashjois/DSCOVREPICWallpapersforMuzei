package com.biryanistudio.dscovrepicformuzei

import android.content.Context
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import org.jetbrains.anko.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        val PREFERENCES_KEY = "com.biryanistudio.dscovrepicformuzei.preferences"
        val REFRESH_TIME = "refreshHour"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        var refreshSeekBar: SeekBar? = null
        verticalLayout {
            textView {
                setTypeface(null, Typeface.BOLD_ITALIC)
                text = "Set refresh time"
                padding = dip(16)
                textSize = 22f
            }.lparams(width = matchParent, height = wrapContent)
            verticalLayout {
                val refreshTimeText = textView {
                    val time = sharedPreferences.getInt(REFRESH_TIME, 1)
                    text = if (time == 0) "Refresh manually" else "Refresh in $time hour(s)"
                    padding = dip(16)
                    textSize = 18f
                }.lparams(width = matchParent, height = wrapContent)
                refreshSeekBar = seekBar {
                    max = 24
                    padding = dip(16)
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar,
                                                       progress: Int, fromUser: Boolean) {
                            sharedPreferences.edit().putInt(REFRESH_TIME, progress).apply()
                            refreshTimeText.text = if (progress == 0) "Refresh manually"
                            else "Refresh in $progress hour(s)"
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar) {}
                    })
                }.lparams(width = matchParent, height = wrapContent)
                textView {
                    text = "The image might not refresh if a newer image has not been made available by the EPIC Team"
                    setTypeface(null, Typeface.ITALIC)
                    textSize = 14f
                }.lparams(width = matchParent, height = wrapContent)
            }.lparams(width = matchParent, height = wrapContent)
            verticalLayout {
                setVerticalGravity(Gravity.BOTTOM)
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
                    text = "This app has been completely designed using Kotlin."
                }.lparams(width = matchParent, height = wrapContent)
                textView {
                    padding = dip(16)
                    textSize = 18f
                    gravity = Gravity.CENTER
                    text = "Made with \u2665 from Bengaluru, India"
                }.lparams(width = matchParent, height = wrapContent)
            }.lparams(width = matchParent, height = matchParent)
        }
        refreshSeekBar?.progress = sharedPreferences.getInt(REFRESH_TIME, 1)
        refreshSeekBar?.refreshDrawableState()
    }
}