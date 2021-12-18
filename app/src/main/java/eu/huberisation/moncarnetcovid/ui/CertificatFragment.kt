package eu.huberisation.moncarnetcovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.databinding.CertificatFragmentBinding
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.entities.Detenteur
import eu.huberisation.moncarnetcovid.helper.BarcodeHelper

class CertificatFragment(): Fragment() {
    private lateinit var binding: CertificatFragmentBinding
    private var onCertificatClickListener: View.OnClickListener? = null
    private var certificat: Certificat? = null

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
            setImage(it)
            setDetenteur(it.detenteur)

            onCertificatClickListener?.let { listener ->
                binding.certificatCard.setOnClickListener(listener)
            }
        }
    }

    fun setOnCardClickListener(listener: View.OnClickListener) {
        onCertificatClickListener = listener
    }

    private fun setImage(certificat: Certificat) {
        val qrCodeSize = resources.getDimension(R.dimen.qr_code_fullscreen_size).toInt()
        binding.certificatCode.setImageBitmap(
            BarcodeHelper.genererBitmap(certificat, qrCodeSize)
        )
    }

    private fun setDetenteur(detenteur: Detenteur) {
        binding.certificatDetenteur.apply {
            text = detenteur.nom
            visibility = View.VISIBLE
        }
    }
}