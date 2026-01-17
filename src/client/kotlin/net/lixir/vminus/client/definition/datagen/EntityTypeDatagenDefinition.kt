package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.Definition
import net.lixir.vminus.client.definition.registry.DefinitionRegistry
import net.minecraft.entity.EntityType
import net.minecraft.registry.tag.TagKey
import java.util.*

open class EntityTypeDatagenDefinition: DatagenDefinition<EntityType<*>>(),
    DefinitionWithTags<EntityTypeDatagenDefinition, EntityType<*>> {
    val tags: MutableSet<TagKey<EntityType<*>>> = HashSet()

    override fun tags(vararg tags: TagKey<EntityType<*>>): EntityTypeDatagenDefinition {
        this.tags.addAll(tags.toList())
        return this
    }

    override fun merge(other: Definition<EntityType<*>>?): Definition<EntityType<*>> {
        if (other == null || other !is EntityTypeDatagenDefinition) return this
        tags.addAll(other.tags)
        return super.merge(other)
    }

    override fun setDefault(of: EntityType<*>): EntityTypeDatagenDefinition {
        if (inherits) {
            val definition = DefinitionRegistry.getDefaultEntityDefinition(of, category)
            this.merge(definition)
        }
        return this
    }

    override val empty: Boolean
        get() = this == EMPTY

    companion object {
        val EMPTY: EntityTypeDatagenDefinition = EntityTypeDatagenDefinition()

        fun ofInheritable(): EntityTypeDatagenDefinition {
            val definition = EntityTypeDatagenDefinition()
            definition.inherits = true
            return definition
        }
    }
}
