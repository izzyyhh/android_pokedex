package com.example.izzypokedex.api

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
}