package com.timmytruong.dogbreeds.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timmytruong.dogbreeds.model.DogBreed
import com.timmytruong.dogbreeds.model.DogDatabase
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application): BaseViewModel(application)
{
    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(dogUuid: Int)
    {
        launch {
            val dog = DogDatabase(getApplication()).dogDao().getDog(dogUuid)
            dogLiveData.value = dog
        }
    }
}
