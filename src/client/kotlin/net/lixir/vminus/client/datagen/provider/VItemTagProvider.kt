package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.lixir.vminus.Vminus
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionRegistry
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

abstract class VItemTagProvider(
    output: FabricDataOutput,
    lookupFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider.ItemTagProvider(output, lookupFuture) {

    protected val modId: String = output.modId

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        val itemDefinitions = DataGenDefinitionRegistry.fromId(modId)?.itemDefinitions ?: return
        for ((item, definition) in itemDefinitions) {
            definition.tags.forEach { tag ->
                Vminus.devLogger("Generated tag $tag for $item")
                getOrCreateTagBuilder(tag).add(item)
            }
        }
    }
}
