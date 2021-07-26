package eu.huberisation.moncarnetcovid.helper

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsHelper {
    private const val SHARED_PREFS_NAME = "eu.huberisation.moncarnetcovid.covid_prefs"
    private const val PREF_ONBOARDING = "onbooarding_termine"

    fun isOnboardingDone(context: Context): Boolean {
        val prefs = getSharedPrefs(context)
        return prefs.getBoolean(
            PREF_ONBOARDING,
            false
        )
    }

    fun setOnboardingDone(context: Context) {
        val prefs = getSharedPrefs(context)
        with(prefs.edit()) {
            putBoolean(
                PREF_ONBOARDING,
                true
            )
            commit()
        }
    }

    private fun getSharedPrefs(context: Context): SharedPreferences {
        return context
            .getSharedPreferences(
                SHARED_PREFS_NAME,
                Context.MODE_PRIVATE
            )
    }
}