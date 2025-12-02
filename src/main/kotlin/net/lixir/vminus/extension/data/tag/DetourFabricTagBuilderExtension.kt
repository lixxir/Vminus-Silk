package net.lixir.vminus.extension.data.tag

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.data.server.tag.TagProvider
import net.minecraft.registry.tag.TagKey

object DetourFabricTagBuilderExtension {
    fun <T> FabricTagProvider<T>.FabricTagBuilder.addOptionalTags(vararg tags: TagKey<T>) {
        for (tag in tags)
            addOptionalTag(tag)
    }
}