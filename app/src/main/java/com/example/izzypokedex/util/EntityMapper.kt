package com.example.izzypokedex.util

interface EntityMapper<Entity, DomainModel> {
    fun mapToDomainModel(entity: Entity) : DomainModel
    fun mapToEntity(domainModel: DomainModel) : Entity
}