package com.example.izzypokedex.di

import com.example.izzypokedex.api.PokeApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideMoshi() : Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideOkHttp() : OkHttpClient = OkHttpClient().newBuilder()
        .readTimeout(50, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    fun pokeApi(retrofit: Retrofit): PokeApi = retrofit.create()
}