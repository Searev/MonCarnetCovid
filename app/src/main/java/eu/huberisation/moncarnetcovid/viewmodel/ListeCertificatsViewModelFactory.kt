package eu.huberisation.moncarnetcovid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.huberisation.moncarnetcovid.data.CertificatRepository

class ListeCertificatsViewModelFactory(private val certificatRepository: CertificatRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListeCertificatsViewModel::class.java)) {
            return ListeCertificatsViewModel(certificatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}