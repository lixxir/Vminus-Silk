package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider
import net.lixir.vminus.Vminus.Companion.devLogger
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionRegistry
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

abstract class VBlockTagProvider(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : BlockTagProvider(output, completableFuture) {

    val modId: String = output.modId

    override fun configure(lookup: RegistryWrapper.WrapperLookup) {
        val definitions = DataGenDefinitionRegistry.fromId(modId)?.blockDefinitions ?: return
        for ((block, definition) in definitions) {
            for (tag in definition.tags) {
                devLogger("Added tag $tag for $block")
                getOrCreateTagBuilder(tag).add(block)
            }
        }
    }
}
