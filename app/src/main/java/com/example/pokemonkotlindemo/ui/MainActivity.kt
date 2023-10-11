package com.example.pokemonkotlindemo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonkotlindemo.base.activity.BaseActivity
import com.example.pokemonkotlindemo.base.utils.init
import com.example.pokemonkotlindemo.bean.PokemonBean
import com.example.pokemonkotlindemo.databinding.ActivityMainBinding
import com.example.pokemonkotlindemo.vm.MainViewModel
import com.example.pokemonkotlindemo.widget.PokemonAdapter
import com.jakewharton.rxbinding4.widget.textChangeEvents
import com.yanzhenjie.recyclerview.OnItemClickListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private val pokemonAdapter: PokemonAdapter by lazy { PokemonAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.input.textChangeEvents()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .filter {
                it.text.length > 0
            }.subscribe {
                Log.i("cwx", "subscribe -------${it.text}")
                mViewModel.searchPokemon(it.text.toString(), 20, 0)
            }

        mDatabind.recyclerview.useDefaultLoadMore()
        mDatabind.recyclerview.setOnItemClickListener { view, position ->
            val pokemonSpecy = pokemonAdapter.data.get(position)
            var pokemonBean: PokemonBean = PokemonBean()
            pokemonBean.name = pokemonSpecy.name
            pokemonSpecy.pokemon_v2_pokemons.forEach {
                it.pokemon_v2_pokemonabilities.forEach {
                    pokemonBean.ability.add(it.pokemon_v2_ability?.name)
                }
            }
            val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                putExtra("pokemon", pokemonBean)
            }
            startActivity(intent)
        }
        mDatabind.recyclerview.init(LinearLayoutManager(this), pokemonAdapter)


        mDatabind.recyclerview.setLoadMoreListener {
            mViewModel.searchPokemon(mDatabind.input.text.toString(), 20, mDatabind.recyclerview.childCount)
        }

    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.pokemonList.observe(this, Observer {
            mDatabind.recyclerview.loadMoreFinish(it.isEmpty(), it.size % 20 == 0)
            pokemonAdapter.setList(it)
        })
    }
}