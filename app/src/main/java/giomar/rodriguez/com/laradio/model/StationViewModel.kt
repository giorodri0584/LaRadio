package giomar.rodriguez.com.laradio.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StationViewModel : ViewModel(){
    val currentStation: MutableLiveData<Station> by lazy {
        MutableLiveData<Station>()
    }
}