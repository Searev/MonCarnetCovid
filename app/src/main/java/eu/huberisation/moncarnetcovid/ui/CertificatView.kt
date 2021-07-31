package eu.huberisation.moncarnetcovid.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.button.MaterialButton
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.entities.CertificatEuropeen
import eu.huberisation.moncarnetcovid.entities.TypeCertificat
import java.text.SimpleDateFormat
import java.util.*

class CertificatView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    init {
        inflate(context, R.layout.certificat_card_view, this)

        context
            .theme
            .obtainStyledAttributes(
                attrs,
                R.styleable.CertificatView,
                0,
                0
            ).apply {
                getString(R.styleable.CertificatView_type)?.let { setType(it) }
                getString(R.styleable.CertificatView_detenteur)?.let { setDetenteur(it) }
                recycle()
            }
    }

    fun setCertificat(certificat: Certificat) {
        val data: Pair<Int, Int> = when (certificat.type) {
            TypeCertificat.TEST -> {
                val description = if (certificat is CertificatEuropeen) {
                    when (certificat.resultatTest) {
                        true -> R.string.test_positif
                        false -> R.string.test_negatif
                        null -> R.string.test
                    }
                } else R.string.test

                Pair(
                    description,
                    R.drawable.icon_test
                )
            }
            TypeCertificat.VACCINATION -> Pair(R.string.vaccination, R.drawable.icon_vaccin)
            TypeCertificat.RETABLISSEMENT -> Pair(R.string.attestation_retablissement, R.drawable.icon_retablissement)
        }

        setType(data.first)
        setImage(data.second)
        setDetenteur(certificat.detenteur)
        setDate(certificat.dateEmission, certificat.type)
    }

    fun setImage(@DrawableRes id: Int) {
        findViewById<ImageView>(R.id.certificatImg).setImageResource(id)
    }

    fun setType(type: String) {
        findViewById<TextView>(R.id.certificatType)?.text = type
    }

    fun setType(@StringRes ressourceId: Int) {
        findViewById<TextView>(R.id.certificatType)?.apply {
            text = context.getString(ressourceId)
            visibility = View.VISIBLE
        }
    }

    fun setDetenteur(type: String) {
        findViewById<TextView>(R.id.certificatDetenteur)?.apply {
            text = type
            visibility = View.VISIBLE
        }
    }

    fun setDate(date: Date?, typeCertificat: TypeCertificat) {
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

        findViewById<TextView>(R.id.certificatDate).apply {
            setText(champDate)
            visibility = View.VISIBLE
        }
    }

    fun setBtnClickListener(listener: OnClickListener) {
        findViewById<MaterialButton>(R.id.afficherCodeBtn)?.setOnClickListener(listener)
    }
}