package com.example.izzypokedex.api

import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.api.models.ApiPokeEvoChain
import com.example.izzypokedex.api.models.ApiPokemon
import com.example.izzypokedex.api.models.ApiPokemonListResponse
import com.example.izzypokedex.api.models.ApiPokemonSpecies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int) : ApiPokemon

    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query( "offset") offset: Int) : ApiPokemonListResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int) : ApiPokemonSpecies

    @GET("evolution-chain/{id}")
    suspend fun getPokeEvoChain(@Path("id") evoChainId: Int) : ApiPokeEvoChain
}

fun getIdFromUrl(url: String): Int {
    val splittedUrl = url.split("/")
    return splittedUrl[splittedUrl.size - 2].toInt()
}

fun getEvoChain(evoChain: ApiPokeEvoChain): List<Int> {
    try {
        val chain = mutableListOf<Int>()
        chain.add(getIdFromUrl(evoChain.chain.species.url))

        var evoData = evoChain.chain.evolvesTo

        while (true) {
            chain.add(getIdFromUrl(evoData[0].species.url))

            evoData = evoData[0].evolvesTo!!

            if (evoData.isEmpty()) break
        }

        return chain

    } catch (e: Exception) {
        return emptyList()
    }
}