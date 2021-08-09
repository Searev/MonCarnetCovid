package eu.huberisation.moncarnetcovid.ui

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
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
import kotlinx.coroutines.launch


class DetailCertificatFragment : Fragment() {

    private lateinit var binding: DetailCertificatFragmentBinding
    private lateinit var certificat: Certificat
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailCertificatTitreAppBar.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_supprimer -> {
                        confirmerSuppression()
                        true
                    }
                    else -> false
                }
            }
        }

        lifecycleScope.launch {
            certificat = (requireActivity().application as MonCarnetCovidApplication)
                .certificatRepository
                .recupererCertificat(args.idCertificat)

            binding.detailCertificatTitreAppBar.title = getString(
                when (certificat.type) {
                    TypeCertificat.VACCINATION -> R.string.certificat_vaccination
                    TypeCertificat.TEST -> R.string.attestation_test
                    TypeCertificat.RETABLISSEMENT -> R.string.attestation_retablissement
                }
            )

            binding.barcodeImageView.apply {
                setImageBitmap(genererCodeBitmap(certificat.code, certificat.getBarcodeFormat(), qrCodeSize))
                setOnClickListener { aggrandirImage(certificat) }
            }

            certificat.detenteur.let {
                binding.barcodeDetenteur.apply {
                    text = it
                    visibility = View.VISIBLE
                }
            }
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
            setOnShowListener { modifierLuminosite(true)  }
            setOnDismissListener { modifierLuminosite(false) }
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
            (requireActivity().application as MonCarnetCovidApplication).certificatRepository
                .supprimerCertificat(certificat)
            findNavController().popBackStack()
        }
    }

    private fun modifierLuminosite(activer: Boolean) {
        activity?.let { activity ->
            val windowInsetsController = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
            val isAppearanceLightStatusBars = windowInsetsController.isAppearanceLightStatusBars
            val params: WindowManager.LayoutParams? = activity.window?.attributes

            if (activer) {
                activity.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                params?.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
            } else {
                activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                params?.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            }

            activity.window?.attributes = params // this call make the status bars loose its appearance
            windowInsetsController.isAppearanceLightStatusBars = isAppearanceLightStatusBars
        }
    }
}