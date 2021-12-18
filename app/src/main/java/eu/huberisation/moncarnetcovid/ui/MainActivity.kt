package eu.huberisation.moncarnetcovid.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.databinding.ActivityMainBinding
import eu.huberisation.moncarnetcovid.helper.SharedPrefsHelper
import eu.huberisation.moncarnetcovid.ui.onboarding.OnboardingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!SharedPrefsHelper.isOnboardingDone(this)) {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!SharedPrefsHelper.isOnboardingDone(this)) {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}