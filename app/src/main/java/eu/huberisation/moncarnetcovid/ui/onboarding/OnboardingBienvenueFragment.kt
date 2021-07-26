package eu.huberisation.moncarnetcovid.ui.onboarding

import androidx.navigation.fragment.findNavController
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.helper.SharedPrefsHelper


class OnboardingBienvenueFragment: OnboardingFragment() {
    override fun getTitleKey() = R.string.onboarding_bienvenue_titre
    override fun getTextKey() = R.string.onboarding_bienvenue_description
    override fun getBtnTextKey() = R.string.onboarding_bienvenue_btn
    override fun getImageKey() = R.drawable.covid_19_precautions

    override fun onBtnClick() {
        SharedPrefsHelper.setOnboardingDone(requireContext())
        val action = OnboardingBienvenueFragmentDirections.actionOnboardingBienvenueFragmentToOnboardingFonctionnementFragment()
        findNavController().navigate(action)
    }
}