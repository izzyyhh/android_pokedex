package com.example.izzypokedex.api

import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.api.models.ApiPokeEvoChain
import com.example.izzypokedex.api.models.ApiPokemon
import com.example.izzypokedex.api.models.ApiPokemonSpecies
import com.example.izzypokedex.db.entities.DbEvoChain
import com.example.izzypokedex.db.entities.DbPokemonSpecies
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

    fun mapApiSpeciesToDbSpecies(apiSpecies: ApiPokemonSpecies) : DbPokemonSpecies {
        return DbPokemonSpecies(
            id = apiSpecies.id,
            text = apiSpecies.textEntries.filter { it.language.name == "en" }[0].text,
            color = apiSpecies.color.name,
            evoChainId = getIdFromUrl(apiSpecies.evolutionChain.url)
        )
    }

    fun mapApiEvoToDbEvo(apiEvo: ApiPokeEvoChain) : DbEvoChain {
        val evoChain: List<Int> = getEvoChain(apiEvo)

        return DbEvoChain(
            id = apiEvo.id,
            stage1 = evoChain.getOrNull(0),
            stage2 = evoChain.getOrNull(1),
            stage3 = evoChain.getOrNull(2)
        )
    }
}