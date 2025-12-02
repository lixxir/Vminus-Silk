package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.EntityTypeTagProvider
import net.lixir.vminus.Vminus.Companion.devLogger
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionRegistry
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import java.util.concurrent.CompletableFuture

abstract class VEntityTypeTagProvider(output: FabricDataOutput, completableFuture: CompletableFuture<WrapperLookup>) :
    EntityTypeTagProvider(output, completableFuture) {

    protected val modId: String = output.modId

    override fun configure(arg: WrapperLookup) {
        val registry = DataGenDefinitionRegistry.fromId(modId) ?: return

        for ((entityType, definition) in registry.entityTypeDefinitions.entries) {
            for (tag in definition.tags) {
                devLogger("Added tag $tag for $entityType")
                getOrCreateTagBuilder(tag).add(entityType)
            }
        }
    }
}