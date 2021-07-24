package eu.huberisation.moncarnetcovid.adapters


import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.huberisation.moncarnetcovid.R
import eu.huberisation.moncarnetcovid.ui.CertificatView
import eu.huberisation.moncarnetcovid.model.Certificat
import eu.huberisation.moncarnetcovid.model.TypeCertificat

class CertificateListeAdapter(private val onCLickListener: OnCertificateClickListener): ListAdapter<Certificat, CertificateListeAdapter.CertificateViewHolder>(CertificateComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateListeAdapter.CertificateViewHolder {
        val view = CertificatView(parent.context, null)
        view.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        return CertificateViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CertificateListeAdapter.CertificateViewHolder,
        position: Int
    ) {
        val current = getItem(position)
        holder.bind(current, onCLickListener)
    }

    class CertificateViewHolder(itemView: CertificatView): RecyclerView.ViewHolder(itemView) {
        fun bind(certificat: Certificat, clickListener: OnCertificateClickListener) {
            itemView as CertificatView
            val typeString = when (certificat.type) {
                TypeCertificat.SANITAIRE -> R.string.certificat_test
                TypeCertificat.VACCINATION -> R.string.attestation_vaccination
            }
            itemView.setType(typeString)
            certificat.detenteur?.let {
                itemView.setDetenteur(it)
            }
            itemView.setBtnClickListener {
                clickListener.onCertificateClick(certificat)
            }
            itemView.setOnCreateContextMenuListener { _, _, _ ->
                clickListener.onCreateContextMenu(certificat)
            }
        }
    }

    class CertificateComparator: DiffUtil.ItemCallback<Certificat>() {
        override fun areItemsTheSame(oldItem: Certificat, newItem: Certificat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Certificat, newItem: Certificat): Boolean {
            return oldItem.contenu == newItem.contenu
        }

    }
}