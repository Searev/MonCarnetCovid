package eu.huberisation.moncarnetcovid.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import eu.huberisation.moncarnetcovid.MonCarnetCovidApplication
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.databinding.ActivityMainBinding
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.entities.CertificatEuropeen
import eu.huberisation.moncarnetcovid.entities.CertificatFactory
import eu.huberisation.moncarnetcovid.exceptions.CertificatInvalideException
import eu.huberisation.moncarnetcovid.helper.SharedPrefsHelper
import eu.huberisation.moncarnetcovid.ui.onboarding.OnboardingActivity
import eu.huberisation.moncarnetcovid.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val TOUS_ANTI_COVID_WALLET = "https://bonjour.tousanticovid.gouv.fr/app/wallet?v="
        const val TOUS_ANTI_COVID_WALLETDCC = "https://bonjour.tousanticovid.gouv.fr/app/walletdcc#"
        const val REQUEST_PERMISSION_CODE = 1
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val permission = Manifest.permission.CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!SharedPrefsHelper.isOnboardingDone(this)) {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)

            setContentView(binding.root)

            binding.fab.setOnClickListener {
                handlePermission()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!SharedPrefsHelper.isOnboardingDone(this)) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.showFabBtn.observe(this) {
            if (it) {
                binding.fab.show()
            } else {
                binding.fab.hide()
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == -1) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result.contents.isNotBlank()) {
                sauvegarderCode(result.contents)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialiserScan()
            } else {
                expliquerPermissionCamera()
            }
        }
    }

    private fun sauvegarderCode(code: String) {
        try {
            val certificat = handleCode(code)
            lifecycleScope.launch {
                (application as MonCarnetCovidApplication)
                    .certificatRepository
                    .creerCertificat(certificat)
            }

            Toast.makeText(
                this,
                R.string.scan_reussi,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: CertificatInvalideException) {
            Toast.makeText(
                this,
                R.string.scan_echec,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleCode(code: String): Certificat {
        val parsedCode = when {
            code.startsWith(TOUS_ANTI_COVID_WALLET) -> Uri.decode(code.substring(TOUS_ANTI_COVID_WALLET.length))
            code.startsWith(TOUS_ANTI_COVID_WALLETDCC) -> Uri.decode(code.substring(TOUS_ANTI_COVID_WALLETDCC.length))
            code.startsWith(CertificatEuropeen.PREFIX_CERTIFICAT) -> code
            else -> throw CertificatInvalideException()
        }
        return CertificatFactory.creerCertificatDepuisCode(parsedCode)
    }

    private fun handlePermission() {
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> initialiserScan()
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
        ActivityCompat.requestPermissions(this, arrayOf(permission),  REQUEST_PERMISSION_CODE)
    }

    private fun initialiserScan() {
        IntentIntegrator(this).apply {
            setPrompt(getString(R.string.scan_instruction))
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
        }.initiateScan()
    }

    private fun expliquerPermissionCamera() {
        AlertDialog.Builder(this)
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

}