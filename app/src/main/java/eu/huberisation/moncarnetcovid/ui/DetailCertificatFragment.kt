package eu.huberisation.moncarnetcovid.ui

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import eu.huberisation.moncarnetcovid.MonCarnetCovidApplication
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.databinding.DetailCertificatFragmentBinding
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.entities.TypeCertificat
import eu.huberisation.moncarnetcovid.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch


class DetailCertificatFragment : Fragment() {

    private lateinit var binding: DetailCertificatFragmentBinding
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val barcodeEncoder = BarcodeEncoder()
    private val args: DetailCertificatFragmentArgs by navArgs()
    private val qrCodeSize by lazy {
        requireContext().resources.getDimension(R.dimen.qr_code_fullscreen_size).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailCertificatFragmentBinding.inflate(inflater, container, false)
        activityViewModel.showFabBtn.postValue(false)
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

            binding.detailCertificatTitre.text = getString(
                when (certificat.type) {
                    TypeCertificat.VACCINATION -> R.string.certificat_vaccination
                    TypeCertificat.TEST -> R.string.attestation_test
                    TypeCertificat.RETABLISSEMENT -> R.string.attestation_retablissement
                }
            )

            binding.barcodeImageView.setImageBitmap(
                genererCodeBitmap(certificat.code, certificat.getBarcodeFormat(), qrCodeSize)
            )
            binding.barcodeImageView.setOnClickListener {
                aggrandirImage(certificat)
            }

            certificat.detenteur.let {
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

    fun aggrandirImage(certificat: Certificat) {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            val nextImageView = ImageView(context).apply {
                setImageBitmap(
                    genererCodeBitmap(
                        certificat.code,
                        certificat.getBarcodeFormat(),
                        width
                    )
                )
            }
            addContentView(nextImageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ))
        }
        dialog.show()
    }

    private fun genererCodeBitmap(code: String, format: BarcodeFormat, size: Int): Bitmap? {
        return barcodeEncoder.encodeBitmap(
            code,
            format,
            size,
            size
        )
    }
}