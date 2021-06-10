package com.example.izzypokedex.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.db.DbMapper
import com.example.izzypokedex.db.PokemonDatabase
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.repository.PokemonListRemoteMediator
import com.example.izzypokedex.repository.PokemonRepository
import com.example.izzypokedex.util.DataState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel
@Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel()
{
    private val _dataState: MutableLiveData<DataState<Pokemon>> = MutableLiveData()
    val dataState: LiveData<DataState<Pokemon>> = _dataState

    fun setEvent(event: PokemonDetailEvent) {
        when(event) {
            is PokemonDetailEvent.GetPokemonEvent -> {
                viewModelScope.launch {
                    pokemonRepository.getPokemon(event.id)
                        .onEach {
                            _dataState.postValue(it)
                        }.launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class PokemonDetailEvent {
    data class GetPokemonEvent(val id: Int): PokemonDetailEvent()
}
