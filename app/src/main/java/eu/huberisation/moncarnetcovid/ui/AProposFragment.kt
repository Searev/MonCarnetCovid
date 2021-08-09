package eu.huberisation.moncarnetcovid.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eu.huberisation.moncarnetcovid.databinding.AProposFragmentBinding

/**
 * Fragment décrivant le fonctionnement de l'application et donnant des informations
 * d'ordre général sur sa création (license, code source...)
 */
class AProposFragment : Fragment() {
    companion object {
        private const val GITHUB_URL = "https://github.com/Searev/MonCarnetCovid"
    }

    lateinit var binding: AProposFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AProposFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.ouvrirGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(GITHUB_URL)
            startActivity(intent)
        }
    }
}