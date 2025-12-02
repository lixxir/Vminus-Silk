package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.lixir.vminus.registry.tag.VminusEntityTypeTags
import net.lixir.vminus.registry.tag.VminusItemTags
import net.lixir.vminus.vision.property.ItemVisionProperties
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import java.util.concurrent.CompletableFuture

class VminusVisionProvider(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<WrapperLookup>)
    : VisionProvider(output, completableFuture) {

    override fun addVisions() {
        itemVision(VminusItemTags.BANNED).apply {
            with(ItemVisionProperties.BAN, true)
        }.save(this, "tag/banned")

        entityTypeVision(VminusEntityTypeTags.BANNED).apply {
            with(ItemVisionProperties.BAN, true)
        }.save(this, "tag/banned")
    }
}
