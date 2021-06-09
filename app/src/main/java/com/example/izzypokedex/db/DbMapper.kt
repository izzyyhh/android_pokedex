package com.example.izzypokedex.db

import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.db.entities.DbPokemon
import com.example.izzypokedex.util.EntityMapper
import javax.inject.Inject

class DbMapper
@Inject
constructor() : EntityMapper<DbPokemon, Pokemon>{
    override fun mapToDomainModel(entity: DbPokemon): Pokemon {
        val entityTypes = listOf<String>(entity.type1, entity.type2)

        return Pokemon(
            id = entity.id,
            name = entity.name,
            frontShiny = entity.frontShiny,
            frontOfficialDefault = entity.frontOfficialDefault,
            types = entityTypes
                .filter {
                    it != ""
                }
        )
    }

    override fun mapToEntity(domainModel: Pokemon): DbPokemon {
        return DbPokemon(
            id = domainModel.id,
            name = domainModel.name,
            frontShiny = domainModel.frontShiny,
            frontOfficialDefault = domainModel.frontOfficialDefault,
            type1 = domainModel.types[0],
            type2 = if(domainModel.types.size > 1) domainModel.types[1] else ""
        )
    }

    fun mapToDomainModelList(dbPokeList: List<DbPokemon>) : List<Pokemon> {
        return dbPokeList.map {
            mapToDomainModel(it)
        }
    }

    fun mapToEntityList(domainModelList: List<Pokemon>) : List<DbPokemon> {
        return domainModelList.map {
            mapToEntity(it)
        }
    }
}