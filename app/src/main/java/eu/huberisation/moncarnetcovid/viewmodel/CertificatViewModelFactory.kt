package eu.huberisation.moncarnetcovid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.huberisation.moncarnetcovid.data.CertificatRepository

class CertificatViewModelFactory(private val certificatRepository: CertificatRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailCertificatViewModel::class.java) -> DetailCertificatViewModel(certificatRepository) as T
            modelClass.isAssignableFrom(ListeCertificatsViewModel::class.java) -> ListeCertificatsViewModel(certificatRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}