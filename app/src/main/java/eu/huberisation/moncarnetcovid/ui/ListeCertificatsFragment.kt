package eu.huberisation.moncarnetcovid.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.zxing.integration.android.IntentIntegrator
import eu.huberisation.moncarnetcovid.MonCarnetCovidApplication
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.adapters.CertificatPagerAdapter
import eu.huberisation.moncarnetcovid.adapters.OnCertificateClickListener
import eu.huberisation.moncarnetcovid.databinding.ListeCertificatsFragmentBinding
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.entities.CertificatEuropeen
import eu.huberisation.moncarnetcovid.entities.CertificatFactory
import eu.huberisation.moncarnetcovid.exceptions.CertificatInvalideException
import eu.huberisation.moncarnetcovid.viewmodel.CertificatViewModelFactory
import eu.huberisation.moncarnetcovid.viewmodel.ListeCertificatsViewModel
import kotlinx.coroutines.launch


class ListeCertificatsFragment : Fragment(), OnCertificateClickListener  {

    companion object {
        const val TOUS_ANTI_COVID_WALLET = "https://bonjour.tousanticovid.gouv.fr/app/wallet?v="
        const val TOUS_ANTI_COVID_WALLETDCC = "https://bonjour.tousanticovid.gouv.fr/app/walletdcc#"
    }

    private lateinit var binding: ListeCertificatsFragmentBinding
    private lateinit var certificatPagerAdapter: CertificatPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var scanLauncher: ActivityResultLauncher<Intent>

    private val permission = Manifest.permission.CAMERA
    private val viewModel: ListeCertificatsViewModel by viewModels {
        CertificatViewModelFactory((requireActivity().application as MonCarnetCovidApplication).certificatRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                initialiserScan()
            } else {
                expliquerPermissionCamera()
            }
        }

        scanLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
            if (intent.resultCode == -1) {
                val result = IntentIntegrator.parseActivityResult(intent.resultCode, intent.data)
                if (result.contents.isNotBlank()) {
                    sauvegarderCode(result.contents)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListeCertificatsFragmentBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        binding.aProposBtn.setOnClickListener {
            val action = ListeCertificatsFragmentDirections.actionListeCertificatsFragmentToAProposFragment()
            findNavController().navigate(action)
        }

        binding.scanPremierCertificatBtn.setOnClickListener { handlePermission() }
        binding.ajoutCertificatBtn.setOnClickListener { handlePermission() }
    }

    override fun onCertificateClick(certificat: Certificat) {
        certificat.id?.let { id ->
            val action = ListeCertificatsFragmentDirections.actionListeCertificatsFragmentToAfficherCodeFragment(id)
            findNavController().navigate(action)
        }
    }

    private fun setupViewPager() {
        val margin = resources.getDimensionPixelSize(R.dimen.margin_36)
        val pageTransformer = ViewPager2.PageTransformer { page, position ->
            page.translationX = -2 * margin * position
        }

        certificatPagerAdapter = CertificatPagerAdapter(this)
        viewPager = binding.listeCertificatsViewPager.apply {
            adapter = certificatPagerAdapter
            setPageTransformer(pageTransformer)
            offscreenPageLimit = 1
        }

        viewModel.certificats.observe(viewLifecycleOwner) {
            certificatPagerAdapter.setItems(it)
        }
    }

    /**
     * Gestion des permissions pour l'accès à la caméra
     */

    private fun handlePermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> initialiserScan()
            devraitAfficherExplicationPermission() -> expliquerPermissionCamera()
            else -> demanderPermissionCamera()
        }
    }

    private fun devraitAfficherExplicationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(permission)
        } else {
            false
        }
    }

    private fun demanderPermissionCamera() {
        requestPermissionLauncher.launch(permission)
    }

    private fun initialiserScan() {
        val intent = IntentIntegrator(requireActivity()).apply {
            setPrompt(getString(R.string.scan_instruction))
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
        }.createScanIntent()

        scanLauncher.launch(intent)
    }

    private fun expliquerPermissionCamera() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.demander_camera)
            .setMessage(R.string.explication_permission)
            .setPositiveButton(R.string.ok) { dialog, id ->
                demanderPermissionCamera()
            }
            .setNegativeButton(R.string.annuler) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun sauvegarderCode(code: String) {
        try {
            val certificat = gererCode(code)
            lifecycleScope.launch {
                (requireActivity().application as MonCarnetCovidApplication)
                    .certificatRepository
                    .creerCertificat(certificat)
            }

            Toast.makeText(
                requireContext(),
                R.string.scan_reussi,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: CertificatInvalideException) {
            Toast.makeText(
                requireContext(),
                R.string.scan_echec,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun gererCode(code: String): Certificat {
        val parsedCode = when {
            code.startsWith(TOUS_ANTI_COVID_WALLET) -> Uri.decode(code.substring(TOUS_ANTI_COVID_WALLET.length))
            code.startsWith(TOUS_ANTI_COVID_WALLETDCC) -> Uri.decode(code.substring(TOUS_ANTI_COVID_WALLETDCC.length))
            code.startsWith(CertificatEuropeen.PREFIX_CERTIFICAT) -> code
            else -> throw CertificatInvalideException()
        }
        return CertificatFactory.creerCertificatDepuisCode(parsedCode)
    }
}