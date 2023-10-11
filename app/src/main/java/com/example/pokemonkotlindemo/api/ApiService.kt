package com.example.pokemonkotlindemo.api

import com.example.pokemonkotlindemo.base.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/pokemon_v2_pokemonspecies/{name}")
    suspend fun searchPokemon(@Path("name") name: String) : ApiResponse<ArrayList<String>>
}