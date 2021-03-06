package eu.huberisation.moncarnetcovid.viewmodel

import androidx.lifecycle.*
import eu.huberisation.moncarnetcovid.data.CertificatRepository

class ListeCertificatsViewModel(private val certificatRepository: CertificatRepository): ViewModel() {
    val certificats = certificatRepository
        .recupererCertificats()
        .asLiveData()

    val aucunCode: LiveData<Boolean> = Transformations
        .map(certificats) { it.isEmpty() }
}