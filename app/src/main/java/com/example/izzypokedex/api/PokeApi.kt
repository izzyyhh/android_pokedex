package com.example.izzypokedex.api

import com.example.izzypokedex.api.models.ApiPokemon
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int) : ApiPokemon
}