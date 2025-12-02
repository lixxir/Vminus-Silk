package net.lixir.vminus.client.definition

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.minecraft.entity.EntityType


interface EntityTypeApplicableDefinitionRegistry<D : AbstractDefinition<D, *>> {
    val entityTypeDefinitions: Object2ObjectMap<EntityType<*>, D>

    fun getEntityTypeDefinition(entityType: EntityType<*>): D?

    fun define(entityType: EntityType<*>, definition: D)
}
