package com.timmytruong.dogbreeds.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timmytruong.dogbreeds.model.DogBreed

class DetailsViewModel: ViewModel()
{
    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch()
    {
        val dog = DogBreed(breedId = "1", dogBreed = "corgi", lifespan = "15", breedGroup = null, bredFor = null, imageUrl = null, temperament = null)
        dogLiveData.value = dog
    }
}
