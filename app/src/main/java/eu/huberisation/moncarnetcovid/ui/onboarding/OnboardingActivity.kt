package eu.huberisation.moncarnetcovid.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.helper.SharedPrefsHelper

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }

    override fun onStart() {
        super.onStart()
        if (SharedPrefsHelper.isOnboardingDone(this)) {
            finish()
        }
    }
}