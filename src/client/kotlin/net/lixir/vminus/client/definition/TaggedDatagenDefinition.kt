package net.lixir.vminus.client.definition

import net.lixir.vminus.client.definition.datagen.DataGenDefinition
import net.minecraft.registry.tag.TagKey

interface TaggedDatagenDefinition<E : DataGenDefinition<E, T>, T> {
    fun tags(vararg tags: TagKey<T>): E
}
