package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.AbstractDefinitionRegistry
import net.lixir.vminus.client.definition.TaggedDatagenDefinition
import net.minecraft.entity.EntityType
import net.minecraft.registry.tag.TagKey
import java.util.*

open class EntityTypeDataGenDefinition : DataGenDefinition<EntityTypeDataGenDefinition, EntityType<*>>(),
    TaggedDatagenDefinition<EntityTypeDataGenDefinition, EntityType<*>> {
    val tags: MutableSet<TagKey<EntityType<*>>> = HashSet()

    @SafeVarargs
    override fun tags(vararg tags: TagKey<EntityType<*>>): EntityTypeDataGenDefinition {
        this.tags.addAll(tags.toList())
        return this
    }

    override fun merge(other: EntityTypeDataGenDefinition?): EntityTypeDataGenDefinition {
        if (other == null) return this
        tags.addAll(other.tags)
        return super.merge(other)
    }

    override fun setDefault(of: EntityType<*>): EntityTypeDataGenDefinition {
        if (isDefaulted) {
            val definition =
                AbstractDefinitionRegistry.getDefaultEntityDefinition(of, type) as EntityTypeDataGenDefinition?
            this.merge(definition)
        }
        return this
    }

    override val isEmpty: Boolean
        get() = this == EMPTY

    companion object {
        val EMPTY: EntityTypeDataGenDefinition = of()
        fun of(): EntityTypeDataGenDefinition {
            return EntityTypeDataGenDefinition()
        }

        @JvmStatic
        fun ofDefault(): EntityTypeDataGenDefinition {
            val definition = EntityTypeDataGenDefinition()
            definition.isDefaulted = true
            return definition
        }
    }
}
