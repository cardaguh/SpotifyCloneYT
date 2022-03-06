package com.pragma.mymusicapp.data.remote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pragma.mymusicapp.data.entities.FavoriteSong
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


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

    fun insert(data: FavoriteSong){

        dataBaseInstance?.favoritesDao()?.insert(data)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({

            },{

            })?.let {
                compositeDisposable.add(it)
            }
    }


    fun checkRecordExist(data: FavoriteSong){

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



    fun getFavoritesList(){
        dataBaseInstance?.favoritesDao()?.getAllRecords()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                if(!it.isNullOrEmpty()){
                    favoritesList.postValue(it)
                }else{
                    favoritesList.postValue(mutableListOf())
                }
            },{
                favoritesList.postValue(mutableListOf())
            })?.let {

                compositeDisposable.add(it)
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