package eu.huberisation.moncarnetcovid.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.button.MaterialButton
import eu.huberisation.moncarnetcovid.R

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

    fun setType(type: String) {
        findViewById<TextView>(R.id.certificatType)?.text = type
    }

    fun setType(@StringRes ressourceId: Int) {
        findViewById<TextView>(R.id.certificatType)?.text = context.getString(ressourceId)
    }

    fun setDetenteur(type: String) {
        findViewById<TextView>(R.id.certificatDetenteur)?.apply {
            text = type
            visibility = View.VISIBLE
        }
    }

    fun setBtnClickListener(listener: OnClickListener) {
        findViewById<MaterialButton>(R.id.afficherCodeBtn)?.setOnClickListener(listener)
    }
}