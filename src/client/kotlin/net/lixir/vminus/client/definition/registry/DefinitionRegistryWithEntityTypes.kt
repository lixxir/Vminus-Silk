package net.lixir.vminus.client.definition.registry

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.lixir.vminus.client.definition.Definition
import net.minecraft.entity.EntityType


interface DefinitionRegistryWithEntityTypes<D : Definition<*>> {
    val entityTypeDefinitions: Object2ObjectMap<EntityType<*>, D>

    fun getEntityTypeDefinition(entityType: EntityType<*>): D?

    fun define(entityType: EntityType<*>, definition: D)
}
