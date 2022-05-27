package com.example.aoopproject

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceScreen
import com.example.aoopproject.classes.Util
import java.util.*


class SettingsActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    lateinit var settingsFragment: SettingsFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        settingsFragment = SettingsFragment()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, settingsFragment)
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        lateinit var mPreferences: PreferenceScreen
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            mPreferences = preferenceScreen
            val prefScreen = preferenceScreen
        }

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        when(key) {
            getString(R.string.theme_pref) -> {
                Util.applyPreferencedTheme(sharedPreferences, this)
            }
            getString(R.string.lang_pref) -> {
                val lang = sharedPreferences.getString(this.getString(R.string.lang_pref), "en")
                val locale = Locale(lang)
                Locale.setDefault(locale)
                val conf = this.resources.configuration

                conf.setLocale(locale);

                val metrics = resources.displayMetrics
                resources.updateConfiguration(conf, metrics)



            }
        }

    }


}