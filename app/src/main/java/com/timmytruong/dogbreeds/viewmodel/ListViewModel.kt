package com.timmytruong.dogbreeds.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.timmytruong.dogbreeds.model.DogBreed
import com.timmytruong.dogbreeds.model.DogDatabase
import com.timmytruong.dogbreeds.model.DogsApiService
import com.timmytruong.dogbreeds.util.NotificationsHelper
import com.timmytruong.dogbreeds.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application) : BaseViewModel(application)
{
    private val dogsService = DogsApiService()

    private val disposable = CompositeDisposable()

    private var prefHelper = SharedPreferencesHelper(getApplication())

    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L // 5 minutes in ns

    val dogs = MutableLiveData<List<DogBreed>>()

    val dogsLoadError = MutableLiveData<Boolean>()

    val loading = MutableLiveData<Boolean>()

    fun refresh()
    {
        checkCacheDuration()

        val updateTime = prefHelper.getUpdateTime()

        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime)
        {
            fetchFromDatabase()
        }
        else
        {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration()
    {
        val cachePreference = prefHelper.getCacheDuration()

        try
        {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        }
        catch (e: NumberFormatException)
        {
            e.printStackTrace()
        }
    }

    private fun fetchFromRemote()
    {
        loading.value = true

        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>()
                {
                    override fun onSuccess(result: List<DogBreed>)
                    {
                        storeDogsLocally(result)
                        Toast.makeText(getApplication(), "Dogs retrieved from API", Toast.LENGTH_SHORT).show()
                        NotificationsHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable)
                    {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun fetchFromDatabase()
    {
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAll()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshBypassCache()
    {
        fetchFromRemote()
    }


    private fun dogsRetrieved(dogsList: List<DogBreed>)
    {
        dogs.value = dogsList
        loading.value = false
        dogsLoadError.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>)
    {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()

            dao.deleteAll()

            val result = dao.insertAll(*list.toTypedArray())

            for (i in list.indices)
            {
                list[i].uuid = result[i].toInt()
            }

            dogsRetrieved(list)
        }

        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared()
    {
        super.onCleared()

        disposable.clear()
    }
}