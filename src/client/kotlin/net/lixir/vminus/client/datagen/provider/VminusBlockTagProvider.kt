package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.lixir.vminus.registry.tag.VminusBlockTags
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture

class VminusBlockTagProvider(output: FabricDataOutput, completableFuture: CompletableFuture<WrapperLookup>) :
    VBlockTagProvider(output, completableFuture) {
    override fun configure(lookup: WrapperLookup) {
        super.configure(lookup)
        getOrCreateTagBuilder(VminusBlockTags.LEASHABLE).apply {
            addOptionalTag(
                BlockTags.FENCES
            )
        }
        getOrCreateTagBuilder(VminusBlockTags.MUSHROOMS).apply {
            add(
                Blocks.RED_MUSHROOM,
                Blocks.BROWN_MUSHROOM
            )
        }
        getOrCreateTagBuilder(VminusBlockTags.GRASSES).apply {
            add(
                Blocks.SHORT_GRASS
            )
        }
    }
}
