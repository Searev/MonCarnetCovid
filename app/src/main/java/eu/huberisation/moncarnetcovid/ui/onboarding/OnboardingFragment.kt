package eu.huberisation.moncarnetcovid.ui.onboarding


import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import eu.huberisation.moncarnetcovid.databinding.OnboardingFragmentBinding

/**
 * Fragment dédié à l'onboarding des utilisateurs.
 */
abstract class OnboardingFragment : Fragment() {
    private lateinit var binding: OnboardingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OnboardingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            onboardingImage.setImageResource(getImageKey())
            onboardingTitre.text = getString(getTitleKey())
            onboardingDescription.text = getString(getTextKey())
            onboardingDescription.movementMethod = LinkMovementMethod.getInstance()
            onboardingBtn.text = getString(getBtnTextKey())
            onboardingBtn.setOnClickListener { onBtnClick() }
        }
    }

    abstract fun getTitleKey(): Int
    abstract fun getTextKey(): Int
    abstract fun getImageKey(): Int
    abstract fun getBtnTextKey(): Int
    abstract fun onBtnClick()
}