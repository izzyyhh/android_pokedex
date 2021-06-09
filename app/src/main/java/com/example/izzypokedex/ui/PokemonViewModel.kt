package com.example.izzypokedex.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.repository.PokemonRepository
import com.example.izzypokedex.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel
@Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel()
{
    private val _dataState: MutableLiveData<DataState<List<Pokemon>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Pokemon>>> = _dataState

    fun setStateEvent(pokemonStateEvent: PokemonStateEvent) {
        viewModelScope.launch {
            when(pokemonStateEvent){
                is PokemonStateEvent.GetPokemonEvent -> {
                    pokemonRepository.getPokemons(20)
                        .onEach {
                            _dataState.postValue(it)
                        }
                        .launchIn(viewModelScope)

                    //pokemonRepository.getPokemon()
                     //   .onEach {
                        //    _dataState.postValue(it)
                        //}
                       // .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class PokemonStateEvent{
    object GetPokemonEvent: PokemonStateEvent()
    object None: PokemonStateEvent()
}