package eu.huberisation.moncarnetcovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.databinding.CertificatFragmentBinding
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.entities.CertificatEuropeen
import eu.huberisation.moncarnetcovid.entities.TypeCertificat
import java.text.SimpleDateFormat
import java.util.*

class CertificatFragment: Fragment() {
    private lateinit var binding: CertificatFragmentBinding
    private var onCertificatClickListener: View.OnClickListener? = null
    private var certificat: Certificat? = null
    private val barcodeEncoder = BarcodeEncoder()

     companion object {
         private const val ARG_CERTIFICAT = "ARG_CERTIFICAT"

         fun newInstance(certificat: Certificat) = CertificatFragment().apply {
             arguments = bundleOf(
                 ARG_CERTIFICAT to certificat
             )
         }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            certificat = args.getSerializable(ARG_CERTIFICAT) as Certificat?
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = CertificatFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        certificat?.let {

            val typeCertificat = when (it.type) {
                TypeCertificat.TEST -> {
                    if (it is CertificatEuropeen) {
                        when (it.resultatTest) {
                            true -> R.string.test_positif
                            false -> R.string.test_negatif
                            null -> R.string.test
                        }
                    } else R.string.test
                }
                TypeCertificat.VACCINATION -> R.string.vaccination
                TypeCertificat.RETABLISSEMENT -> R.string.attestation_retablissement
            }

            setType(typeCertificat)
            setImage(it.code, it.getBarcodeFormat())
            setDetenteur(it.detenteur)
            setDate(it.dateEmission, it.type)

            onCertificatClickListener?.let {
                binding.certificatCard.setOnClickListener(it)
            }
        }
    }

    fun setOnCardClickListener(listener: View.OnClickListener) {
        onCertificatClickListener = listener
    }

    private fun setImage(code: String, format: BarcodeFormat) {
        val qrCodeSize = resources.getDimension(R.dimen.qr_code_fullscreen_size).toInt()
        binding.certificatCode.setImageBitmap(
            barcodeEncoder.encodeBitmap(
                code,
                format,
                qrCodeSize,
                qrCodeSize
            )
        )
    }

    private fun setType(@StringRes ressourceId: Int) {
        binding.certificatType.apply {
            text = context.getString(ressourceId)
            visibility = View.VISIBLE
        }
    }

    private fun setDetenteur(type: String) {
        binding.certificatDetenteur.apply {
            text = type
            visibility = View.VISIBLE
        }
    }

    private fun setDate(date: Date?, typeCertificat: TypeCertificat) {
        if (date == null) {
            return
        }

        val champDate = when (typeCertificat) {
            TypeCertificat.TEST -> resources.getString(
                R.string.certificat_date_prelevement,
                SimpleDateFormat("dd/MM/yyyy' à 'HH:mm", Locale.FRANCE).format(date)
            )
            TypeCertificat.VACCINATION -> resources.getString(
                R.string.certificat_date_injection,
                SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(date)
            )
            TypeCertificat.RETABLISSEMENT -> resources.getString(
                R.string.certificat_date_validite,
                SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(date)
            )
        }

        binding.certificatDate.apply {
            text = champDate
            visibility = View.VISIBLE
        }
    }
}