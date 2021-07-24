package eu.huberisation.moncarnetcovid.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import eu.huberisation.moncarnetcovid.MonCarnetCovidApplication
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.databinding.ActivityMainBinding
import eu.huberisation.moncarnetcovid.exceptions.CertificatInvalideException
import eu.huberisation.moncarnetcovid.model.TypeCertificat
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val TOUS_ANTI_COVID_WALLET = "https://bonjour.tousanticovid.gouv.fr/app/wallet?v="
        const val TOUS_ANTI_COVID_WALLETDCC = "https://bonjour.tousanticovid.gouv.fr/app/walletdcc#"
        const val PASS_SANITAIRE_EUROPEEN = "HC1:"
        const val REQUEST_PERMISSION_CODE = 1
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val permission = Manifest.permission.CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
            handlePermission()
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
                saveCode(result.contents)

                Toast.makeText(
                    this,
                    R.string.scan_reussi,
                    Toast.LENGTH_LONG
                ).show()
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

    private fun saveCode(code: String) {
        try {
            val infos = handleCode(code)
            lifecycleScope.launch {
                (application as MonCarnetCovidApplication)
                    .certificatRepository
                    .creerCertificat(
                        infos.first,
                        infos.second
                    )
            }
        } catch (e: CertificatInvalideException) {
            Toast.makeText(
                this,
                R.string.scan_echec,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleCode(code: String): Pair<TypeCertificat, String> {
        return when {
            code.startsWith(TOUS_ANTI_COVID_WALLET) -> {
                Pair(TypeCertificat.VACCINATION, Uri.decode(code.substring(TOUS_ANTI_COVID_WALLET.length)))
            }
            code.startsWith(TOUS_ANTI_COVID_WALLETDCC) -> {
                Pair(TypeCertificat.SANITAIRE, Uri.decode(code.substring(TOUS_ANTI_COVID_WALLETDCC.length)))
            }
            code.startsWith(PASS_SANITAIRE_EUROPEEN) -> Pair(TypeCertificat.SANITAIRE, code)
            else -> throw CertificatInvalideException()
        }
    }

    private fun handlePermission() {
        when {
            checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED -> initialiserScan()
            shouldShowRequestPermissionRationale(permission) -> expliquerPermissionCamera()
            else -> requestPermissions(arrayOf(permission), REQUEST_PERMISSION_CODE)
        }
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
                requestPermissions(arrayOf(permission), REQUEST_PERMISSION_CODE)
            }
            .setNegativeButton(R.string.annuler) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}