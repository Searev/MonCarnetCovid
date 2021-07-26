package eu.huberisation.moncarnetcovid.ui.onboarding

import androidx.navigation.fragment.findNavController
import eu.huberisation.moncarnetcovid.R


class OnboardingFonctionnementFragment: OnboardingFragment() {
    override fun getTitleKey() = R.string.onboarding_fonctionnement_titre
    override fun getTextKey() = R.string.onboarding_fonctionnement_description
    override fun getBtnTextKey() = R.string.onboarding_fonctionnement_btn
    override fun getImageKey() = R.drawable.code_smartphone

    override fun onBtnClick() {
        val action = OnboardingFonctionnementFragmentDirections.actionOnboardingFonctionnementFragmentToOnboardingPrivacyFragment()
        findNavController().navigate(action)
    }
}