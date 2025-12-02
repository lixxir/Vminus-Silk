package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import java.util.concurrent.CompletableFuture

class VminusLanguageProvider(dataOutput: FabricDataOutput, registryLookup: CompletableFuture<WrapperLookup>) :
    VLanguageProvider(dataOutput, "en_us", registryLookup)
