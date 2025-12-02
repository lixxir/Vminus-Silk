package net.lixir.vminus.client

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.lixir.vminus.Vminus
import net.lixir.vminus.Vminus.Companion.REGISTRY
import net.lixir.vminus.client.VminusClient.Companion.DATAGEN_DEFINITION_REGISTRY
import net.lixir.vminus.client.datagen.*
import net.lixir.vminus.client.datagen.provider.*
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionRegistry
import net.lixir.vminus.registry.VRegistry

class VminusDataGenerator : VDataGenerator(), DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        super.onInitializeDataGenerator(fabricDataGenerator)
        pack.addProvider(::VminusItemTagProvider)
        pack.addProvider(::VminusBlockTagProvider)
        pack.addProvider(::VminusModelProvider)
        pack.addProvider(::VminusLanguageProvider)
        pack.addProvider(::VminusEntityTypeTagProvider)
        pack.addProvider(::VminusVisionProvider)
    }

    override val vRegistry: VRegistry = REGISTRY

    override val definitionRegistry: DataGenDefinitionRegistry = DATAGEN_DEFINITION_REGISTRY
}
