package eu.huberisation.moncarnetcovid.adapters

import eu.huberisation.moncarnetcovid.model.Certificat

interface OnCertificateClickListener {
    fun onCertificateClick(certificat: Certificat)
    fun onCreateContextMenu(certificat: Certificat)
}