package com.example.izzypokedex.api

import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.Stats
import com.example.izzypokedex.api.models.ApiPokemon
import com.example.izzypokedex.util.EntityMapper
import javax.inject.Inject

class ApiMapper
@Inject
constructor() : EntityMapper<ApiPokemon, Pokemon>{

    override fun mapToDomainModel(entity: ApiPokemon): Pokemon {
        return Pokemon(
            id = entity.id,
            name = entity.name,
            frontShiny = entity.sprites.frontShiny ?: "",
            frontOfficialDefault = entity.sprites.other.officialArtwork.frontDefault ?: "",
            types = entity.types
                .map{
                    it.type.name
                }
        )
    }

    override fun mapToEntity(domainModel: Pokemon): ApiPokemon {
        // not really necessary here, as we are not posting data to the api
        return ApiPokemon(
            id = domainModel.id,
            name = domainModel.name
        )
    }

    fun mapToDomainModelList(apiPokeList: List<ApiPokemon>) : List<Pokemon> {
        return apiPokeList.map {
            mapToDomainModel(it)
        }
    }
}