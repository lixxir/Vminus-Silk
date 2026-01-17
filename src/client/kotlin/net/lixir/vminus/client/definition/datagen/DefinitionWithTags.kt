package net.lixir.vminus.client.definition.datagen

import net.minecraft.registry.tag.TagKey

interface DefinitionWithTags<E : DatagenDefinition<T>, T> {
    fun tags(vararg tags: TagKey<T>): E
}
