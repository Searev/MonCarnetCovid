package eu.huberisation.moncarnetcovid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    val showFabBtn = MutableLiveData(true)
}