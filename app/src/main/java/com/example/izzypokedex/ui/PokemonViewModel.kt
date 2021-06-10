package com.example.izzypokedex.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.db.DbMapper
import com.example.izzypokedex.db.PokemonDatabase
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.repository.PokemonListRemoteMediator
import com.example.izzypokedex.repository.PokemonRepository
import com.example.izzypokedex.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel
@Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val dbMapper: DbMapper,
    private val pokemonDao: PokemonDao
) : ViewModel()
{
    private val _dataState: MutableLiveData<DataState<List<Pokemon>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Pokemon>>> = _dataState

    @ExperimentalPagingApi
    val pokemon: Flow<PagingData<Pokemon>> = pokemonRepository.getPaging(20)

    fun setStateEvent(pokemonStateEvent: PokemonStateEvent) {
        viewModelScope.launch {
            when(pokemonStateEvent){
                is PokemonStateEvent.GetPokemonEvent -> {
                    pokemonRepository.getPokemons(25)
                        .onEach {
                            _dataState.postValue(it)
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class PokemonStateEvent{
    object GetPokemonEvent: PokemonStateEvent()
    object None: PokemonStateEvent()
}
