package com.varivoda.igor.tvz.financijskimanager.ui.settings

import android.app.KeyguardManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.preference.*
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.showSelectedToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

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

    fun setBrightness(float: Float) {
        val lp = window.attributes
        lp.screenBrightness = float
        window.attributes = lp
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private var fingerprintManager: FingerprintManager? = null
        private var keyguardManager: KeyguardManager? = null
        private val settingsViewModel by viewModels<SettingsViewModel> {
            SettingsViewModelFactory((requireContext().applicationContext as App).loginRepository,
                (requireContext().applicationContext as App).preferences)
        }
        private var fingerprint: SwitchPreference? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            settingsViewModel.result.observe(viewLifecycleOwner, Observer {
                if(it==null) return@Observer
                if(it is NetworkResult.Success){

                }else{
                    fingerprint?.isChecked = false
                    showSelectedToast(requireContext(), getString(R.string.problem_with_fingerprint))
                }
            })
            return super.onCreateView(inflater, container, savedInstanceState)
        }
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val pref = Preferences(requireContext())
            /*(activity as SettingsActivity).setBrightness(pref.getSeekBarValue())*/
            findPreference<SeekBarPreference>("brightness key")?.setOnPreferenceChangeListener { _, newValue ->
                val value = (newValue as Int) * 0.01
                (activity as SettingsActivity).setBrightness(value.toFloat())
                pref.setSeekBarValue(value.toFloat())
                return@setOnPreferenceChangeListener true
            }

            findPreference<ListPreference>("toast key")?.setOnPreferenceChangeListener { _, value ->
                Preferences(requireContext()).setToastDesign(value as String)
                showSelectedToast(requireActivity(),getString(R.string.toast_design))
                return@setOnPreferenceChangeListener true
            }

            keyguardManager =
                requireActivity().getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            try {
                fingerprintManager =
                    requireActivity().getSystemService(FINGERPRINT_SERVICE) as FingerprintManager
            } catch (ex: java.lang.Exception) {
                Timber.d(ex)
                return
            }

            fingerprint = findPreference<SwitchPreference>("fingerprint key")
            if (fingerprintManager!!.isHardwareDetected()) {

                if (fingerprintManager!!.hasEnrolledFingerprints() && keyguardManager!!.isKeyguardSecure()) {
                    fingerprint?.isVisible = true
                    fingerprint?.setOnPreferenceChangeListener { preference, newValue ->
                        if(newValue as Boolean){
                            settingsViewModel.addFingerprint()
                            true

                        }else{
                            true
                        }

                    }
                }
            }

        }
    }
}