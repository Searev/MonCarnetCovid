package eu.huberisation.moncarnetcovid.ui.onboarding

import android.content.Intent
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.ui.MainActivity


class OnboardingPrivacyFragment: OnboardingFragment() {
    override fun getTitleKey() = R.string.onboarding_privacy_titre
    override fun getTextKey() = R.string.onboarding_privacy_description
    override fun getBtnTextKey() = R.string.onboarding_privacy_btn
    override fun getImageKey() = R.drawable.secure_data

    override fun onBtnClick() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }
}