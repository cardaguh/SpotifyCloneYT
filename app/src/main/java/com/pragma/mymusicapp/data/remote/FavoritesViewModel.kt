package com.pragma.mymusicapp.data.remote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pragma.mymusicapp.data.entities.FavoriteSong
import com.pragma.mymusicapp.data.entities.Song
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
   // private lateinit var favoritesRepository: FavoritesRepository
    private var dataBaseInstance: FavoriteSongsDatabase ?= null
    // var allFavoritesData: LiveData<MutableList<TouristicSite>>
    private val compositeDisposable = CompositeDisposable()
    var favoritesList = MutableLiveData<MutableList<FavoriteSong>>()

    var checkRecord = MutableLiveData<Pair<FavoriteSong,Boolean>>()

//    fun insert(searchDataModel: TouristicSite?) {
//        favoritesRepository.insert(searchDataModel)
//    }

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    fun insert(data: FavoriteSong){

/*
        dataBaseInstance?.favoritesDao()?.insert(data)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({

            },{

            })?.let {
                compositeDisposable.add(it)
            }
*/
    }


    fun checkRecordExist(data: FavoriteSong){

/*
        dataBaseInstance?.favoritesDao()?.isRecordExists(data.id)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                if(it!=null){
                    checkRecord.postValue(Pair(data,true))
                }else{
                    checkRecord.postValue(Pair(data,false))
                }
            },{
                checkRecord.postValue(Pair(data,false))
            })?.let {
                compositeDisposable.add(it)
            }
*/
    }

    fun checkIsFavourite(song: Song?){
        viewModelScope.launch {
            _isFavourite.value = dataBaseInstance?.favoritesDao()?.isRecordExists(song?.mediaId)!=null
        }
    }

    fun delete(data: FavoriteSong){
        dataBaseInstance?.favoritesDao()?.deleteSingleRecord(data)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                getFavoritesList()
            },{

            })?.let {
                compositeDisposable.add(it)
            }
    }


    fun onFavouriteClick(song: Song?){
        if(song==null)
            return
        viewModelScope.launch {
            val found = dataBaseInstance?.favoritesDao()?.isRecordExists(song.mediaId)
            if(found==null)
                dataBaseInstance?.favoritesDao()?.insert(FavoriteSong(song.mediaId,song.title, song.imageUrl))
            else
                dataBaseInstance?.favoritesDao()?.deleteSingleRecord(song.mediaId)
            _isFavourite.value = found==null
        }
    }

    fun getFavoritesList() = viewModelScope.launch {
        dataBaseInstance?.favoritesDao()?.getAllRecords().let { list->
            favoritesList.value = list
        }
    }



    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }



    fun setInstanceOfDb(dataBaseInstance: FavoriteSongsDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

}