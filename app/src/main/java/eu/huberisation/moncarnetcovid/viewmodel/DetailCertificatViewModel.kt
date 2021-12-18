package eu.huberisation.moncarnetcovid.viewmodel

import androidx.lifecycle.*
import eu.huberisation.moncarnetcovid.data.CertificatRepository
import eu.huberisation.moncarnetcovid.entities.TypeCertificat

class DetailCertificatViewModel(private val certificatRepository: CertificatRepository): ViewModel() {
    private val certificatId = MutableLiveData<Long>()

    val afficherDetails = MutableLiveData(false)
    val afficherDetailsVaccin = afficherDetails.map { it && certificat.value?.type == TypeCertificat.VACCINATION }
    val afficherDetailsTest = afficherDetails.map { it && certificat.value?.type == TypeCertificat.TEST }
    val afficherDetailsRetabllissement =  afficherDetails.map { it && certificat.value?.type == TypeCertificat.RETABLISSEMENT }

    val certificat = certificatId
        .switchMap { certificatRepository.recupererCertificat(it).asLiveData() }

    fun setIdCertificat(id: Long) {
        certificatId.postValue(id)
    }

    fun toggleDetails() {
        afficherDetails.postValue(!(afficherDetails.value ?: false))
    }
}