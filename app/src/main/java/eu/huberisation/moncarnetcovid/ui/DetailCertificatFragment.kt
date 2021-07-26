package eu.huberisation.moncarnetcovid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.journeyapps.barcodescanner.BarcodeEncoder
import eu.huberisation.moncarnetcovid.MonCarnetCovidApplication
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.databinding.DetailCertificatFragmentBinding
import kotlinx.coroutines.launch


class AfficherCodeFragment : Fragment() {

    private lateinit var binding: DetailCertificatFragmentBinding
    private val barcodeEncoder = BarcodeEncoder()
    private val args: AfficherCodeFragmentArgs by navArgs()
    private val qrCodeSize by lazy {
        requireContext().resources.getDimension(R.dimen.qr_code_fullscreen_size).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailCertificatFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailCertificatBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            val certificat = (requireActivity().application as MonCarnetCovidApplication)
                .certificatRepository
                .recupererCertificat(args.idCertificat)

            binding.barcodeImageView.setImageBitmap(
                barcodeEncoder.encodeBitmap(
                    certificat.code,
                    certificat.getBarcodeFormat(),
                    qrCodeSize,
                    qrCodeSize
                )
            )

            certificat.detenteur?.let {
                binding.barcodeDetenteur.apply {
                    text = it
                    visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * Gestion de la luminosité pour cet écran.
     * Lorsque ce fragment apparaît, forcer la luminosité à passer au maximum.
     * Lorsqu'il disparait, faire en sorte que les paramètres par défaut s'appliquent à nouveau.
     */
    override fun onResume() {
        super.onResume()
        activity?.let { activity ->
            val windowInsetsController = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
            val isAppearanceLightStatusBars = windowInsetsController.isAppearanceLightStatusBars
            activity.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            val params: WindowManager.LayoutParams? = activity.window?.attributes
            params?.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
            activity.window?.attributes = params // this call make the status bars loose its appearance
            windowInsetsController.isAppearanceLightStatusBars = isAppearanceLightStatusBars
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let { activity ->
            val windowInsetsController = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
            val isAppearanceLightStatusBars = windowInsetsController.isAppearanceLightStatusBars
            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            val params: WindowManager.LayoutParams? = activity.window?.attributes
            params?.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            activity.window?.attributes = params // this call make the status bars loose its appearance
            windowInsetsController.isAppearanceLightStatusBars = isAppearanceLightStatusBars
        }
    }
}