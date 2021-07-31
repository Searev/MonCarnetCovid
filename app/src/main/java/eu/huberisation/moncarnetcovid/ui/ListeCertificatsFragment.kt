package eu.huberisation.moncarnetcovid.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eu.huberisation.moncarnetcovid.MonCarnetCovidApplication
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.adapters.CertificateListeAdapter
import eu.huberisation.moncarnetcovid.adapters.OnCertificateClickListener
import eu.huberisation.moncarnetcovid.databinding.ListeCertificatsFragmentBinding
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.viewmodel.ListeCertificatsViewModel
import eu.huberisation.moncarnetcovid.viewmodel.ListeCertificatsViewModelFactory
import eu.huberisation.moncarnetcovid.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch


class ListeCertificatsFragment : Fragment(), OnCertificateClickListener  {

    private lateinit var binding: ListeCertificatsFragmentBinding
    private lateinit var adapter: CertificateListeAdapter
    private val viewModel: ListeCertificatsViewModel by viewModels {
        ListeCertificatsViewModelFactory((requireActivity().application as MonCarnetCovidApplication).certificatRepository)
    }
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private var certificatSelectionne: Certificat? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListeCertificatsFragmentBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        adapter = CertificateListeAdapter(this)

        val recyclerView = binding.listeCertificats
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        registerForContextMenu(recyclerView)

        activityViewModel.showFabBtn.postValue(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.certificats.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onCertificateClick(certificat: Certificat) {
        certificat.id?.let { id ->
            val action = ListeCertificatsFragmentDirections.actionListeCertificatsFragmentToAfficherCodeFragment(id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateContextMenu(certificat: Certificat) {
        certificatSelectionne = certificat
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity
            ?.menuInflater
            ?.inflate(
                R.menu.menu_main,
                menu
            )
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                confirmerSuppression()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmerSuppression() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.effacer_certificat_titre)
            .setMessage(R.string.effacer_certificat_confirmation)
            .setPositiveButton(R.string.effacer_certificat) { dialog, id ->
                supprimerCertificat()
            }
            .setNegativeButton(R.string.annuler) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun supprimerCertificat() {
        lifecycleScope.launch {
            certificatSelectionne?.let {
                (requireActivity().application as MonCarnetCovidApplication).certificatRepository
                    .supprimerCertificat(it)
            }
        }
    }
}