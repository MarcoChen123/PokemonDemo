package com.example.pokemonkotlindemo.ui

import android.os.Bundle
import com.example.pokemonkotlindemo.base.activity.BaseActivity
import com.example.pokemonkotlindemo.bean.PokemonBean
import com.example.pokemonkotlindemo.databinding.ActivityDetailBinding
import com.example.pokemonkotlindemo.vm.DetailViewModel
import com.jakewharton.rxbinding4.appcompat.navigationClicks

class PokemonDetailActivity : BaseActivity<DetailViewModel, ActivityDetailBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        val pokemonBean = intent.getSerializableExtra("pokemon") as PokemonBean
        mDatabind.pokemon = pokemonBean
        mDatabind.toolbar.navigationClicks().subscribe{
            finish()
        }

    }
}