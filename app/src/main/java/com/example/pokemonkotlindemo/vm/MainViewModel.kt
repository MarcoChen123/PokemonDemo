package com.example.pokemonkotlindemo.vm

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pokemonkotlindemo.base.MyApplication
import com.example.pokemonkotlindemo.base.utils.request
import com.example.pokemonkotlindemo.base.viewmodel.BaseViewModel
import com.example.pokemonkotlindemo.http.HttpRequestCoroutine
import com.example.pokmonkotlindemo.NameSearchQuery
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {
    var pokemonList: MutableLiveData<ArrayList<NameSearchQuery.Pokemon_v2_pokemonspecy>> = MutableLiveData()
    fun searchPokemon(name: String, limit: Int, offset: Int) {
//        request({ HttpRequestCoroutine.searchPokemon(name)}, {
//            Log.i("cwx", "http error -------- $it")
//        }, {
//            Toast.makeText(MyApplication.context, it.errorMsg, Toast.LENGTH_SHORT).show()
//            Log.i("cwx", "http error -------- ${it.errorMsg}")
//        })

        viewModelScope.launch {
            loadingChange.showDialog.postValue("请求网络中...")
            val listData:ArrayList<NameSearchQuery.Pokemon_v2_pokemonspecy> = HttpRequestCoroutine.searchPokemon(name, limit, offset)
            if(offset == 0) {
                pokemonList.postValue(listData)
            } else {
                pokemonList.value?.addAll(listData)
                pokemonList.postValue(pokemonList.value)
            }
            loadingChange.dismissDialog.postValue(false)
        }
    }
}