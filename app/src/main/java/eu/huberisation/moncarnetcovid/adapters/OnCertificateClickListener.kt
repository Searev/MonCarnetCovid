package eu.huberisation.moncarnetcovid.adapters

import eu.huberisation.moncarnetcovid.entities.Certificat

interface OnCertificateClickListener {
    fun onCertificateClick(certificat: Certificat)
}