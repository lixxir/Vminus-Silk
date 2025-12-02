package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.lixir.vminus.registry.tag.VminusEntityTypeTags
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import java.util.concurrent.CompletableFuture

class VminusEntityTypeTagProvider(output: FabricDataOutput, completableFuture: CompletableFuture<WrapperLookup>) :
    VEntityTypeTagProvider(output, completableFuture) {
    override fun configure(arg: WrapperLookup) {
        super.configure(arg)
        getOrCreateTagBuilder(VminusEntityTypeTags.BANNED)
        getOrCreateTagBuilder(VminusEntityTypeTags.ZOMBIES).apply {
            add(
                EntityType.HUSK,
                EntityType.DROWNED,
                EntityType.ZOMBIE
            )
        }
    }
}
