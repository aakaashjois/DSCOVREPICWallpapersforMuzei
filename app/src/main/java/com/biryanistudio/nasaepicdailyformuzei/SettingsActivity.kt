package com.biryanistudio.nasaepicdailyformuzei

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Switch
import org.jetbrains.anko.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        val DEFAULT_REFRESH_TIME = 3
        val DEFAULT_IMAGE_TYPE = false
        val PREFERENCES_KEY = "com.biryanistudio.nasaepicdailyformuzei.preferences"
        val REFRESH_TIME = "refreshHour"
        val IMAGE_TYPE = "imageType"
    }

    val SEEKBAR_OFFSET = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        var refreshSeekBar: SeekBar? = null
        var imageType: CheckBox? = null
        verticalLayout {
            imageType = checkBox {
                text = "Use enhanced images"
                padding = dip(16)
                textSize = 22f
                setOnCheckedChangeListener { _, isChecked ->
                    sharedPreferences.edit().putBoolean(IMAGE_TYPE, isChecked).apply()
                }
            }.lparams(width = matchParent, height = wrapContent) {margin = dip(16)}
            textView {
                setTypeface(null, Typeface.BOLD_ITALIC)
                text = "Set refresh time"
                padding = dip(16)
                textSize = 22f
            }.lparams(width = matchParent, height = wrapContent)
            verticalLayout {
                val refreshTimeText = textView {
                    val time = sharedPreferences.getInt(REFRESH_TIME, DEFAULT_REFRESH_TIME)
                    text = if (time == 0) "Refresh manually" else "Refresh in $time hours"
                    padding = dip(16)
                    textSize = 18f
                }.lparams(width = matchParent, height = wrapContent)
                refreshSeekBar = seekBar {
                    max = 24 - SEEKBAR_OFFSET
                    padding = dip(16)
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar,
                                                       progress: Int, fromUser: Boolean) {
                            val refreshTime = progress + SEEKBAR_OFFSET
                            sharedPreferences.edit().putInt(REFRESH_TIME, refreshTime).apply()
                            refreshTimeText.text = if (progress == 0) "Refresh manually"
                            else "Refresh every $refreshTime hours"
                        }
                        override fun onStartTrackingTouch(seekBar: SeekBar) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar) {}
                    })
                }.lparams(width = matchParent, height = wrapContent)
                textView {
                    padding = dip(16)
                    text = "A new image is made available roughly once every 3 hours\nSometimes it might take longer to update if a new image has not been made available by the NASA EPIC Team"
                    setTypeface(null, Typeface.ITALIC)
                    gravity = Gravity.CENTER
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
                    text = "This app has been completely designed using Kotlin"
                }.lparams(width = matchParent, height = wrapContent)
                textView {
                    padding = dip(16)
                    textSize = 18f
                    gravity = Gravity.CENTER
                    text = "Made with \u2665 from Bengaluru, India"
                }.lparams(width = matchParent, height = wrapContent)
            }.lparams(width = matchParent, height = matchParent)
        }
        imageType?.isChecked = sharedPreferences.getBoolean(IMAGE_TYPE, DEFAULT_IMAGE_TYPE)
        refreshSeekBar?.progress = sharedPreferences.getInt(REFRESH_TIME, DEFAULT_REFRESH_TIME) - SEEKBAR_OFFSET
        refreshSeekBar?.refreshDrawableState()
    }
}