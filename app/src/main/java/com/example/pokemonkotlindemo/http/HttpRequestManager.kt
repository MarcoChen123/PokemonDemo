package com.example.pokemonkotlindemo.http

import android.util.Log
import com.apollographql.apollo3.api.Optional
import com.example.pokemonkotlindemo.base.network.ApiResponse
import com.example.pokmonkotlindemo.NameSearchQuery
import com.example.pokmonkotlindemo.type.Pokemon_v2_pokemonspecies_bool_exp
import com.example.pokmonkotlindemo.type.String_comparison_exp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

val HttpRequestCoroutine: HttpRequestManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpRequestManager()
}

class HttpRequestManager {

    suspend fun searchPokemon(name: String): ApiResponse<ArrayList<String>> {
        return withContext(Dispatchers.IO) {
            val list = async { apiService.searchPokemon(name) }
            list.await()
        }
    }

    suspend fun searchPokemon(
        name: String,
        limit: Int,
        offset: Int
    ): ArrayList<NameSearchQuery.Pokemon_v2_pokemonspecy> {
        return withContext(Dispatchers.IO) {
            var like = String_comparison_exp(_like = Optional.present("%$name%"))
            var pokemonSpecyBoolExp =
                Pokemon_v2_pokemonspecies_bool_exp(name = Optional.present(like))
            val response =
                NetworkApi.INSTANCE.getApollo().query(
                    NameSearchQuery(
                        limit,
                        offset,
                        where = Optional.present(pokemonSpecyBoolExp)
                    )
                )
                    .execute()
            val listData = response.data?.pokemon_v2_pokemonspecies
            return@withContext listData!!

        } as ArrayList<NameSearchQuery.Pokemon_v2_pokemonspecy>

    }
}