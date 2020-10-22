package com.varivoda.igor.tvz.financijskimanager.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.settings)
    }

    fun setBrightness(float: Float){
        val lp = window.attributes
        lp.screenBrightness = float
        window.attributes = lp
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val pref = Preferences(requireContext())
            (activity as SettingsActivity).setBrightness(pref.getSeekBarValue())
            findPreference<SeekBarPreference>("brightness key")?.setOnPreferenceChangeListener { _, newValue ->
                    val value = (newValue as Int) * 0.01
                (activity as SettingsActivity).setBrightness(value.toFloat())
                pref.setSeekBarValue(value.toFloat())
                return@setOnPreferenceChangeListener true
            }
        }
    }
}