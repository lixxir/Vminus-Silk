package net.lixir.vminus.client

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.lixir.vminus.Vminus.Companion.ID
import net.lixir.vminus.Vminus.Companion.REGISTRY
import net.lixir.vminus.client.datagen.*
import net.lixir.vminus.client.datagen.provider.*
import net.lixir.vminus.client.definition.datagen.DatagenDefinitionRegistry
import net.lixir.vminus.client.definition.datagen.VminusBlockDatagenDefinitionProvider
import net.lixir.vminus.client.definition.datagen.VminusItemDatagenDefinitionProvider

class VminusDataGenerator : VDataGenerator(), DataGeneratorEntrypoint {

    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        if (!System.getProperty("vminus.datagen", "false").toBoolean()) return
        DEFINITION_REGISTRY.addDefinitionProvider(VminusBlockDatagenDefinitionProvider())
        DEFINITION_REGISTRY.addDefinitionProvider(VminusItemDatagenDefinitionProvider())
        super.onInitializeDataGenerator(fabricDataGenerator)
        pack.addProvider(::VminusItemTagProvider)
        pack.addProvider(::VminusBlockTagProvider)
        pack.addProvider(::VminusModelProvider)
        pack.addProvider(::VminusLanguageProvider)
        pack.addProvider(::VminusEntityTypeTagProvider)
        pack.addProvider(::VminusVisionProvider)
    }

    override val vRegistry = REGISTRY

    override val definitionRegistry = DEFINITION_REGISTRY

    companion object {
        val DEFINITION_REGISTRY = DatagenDefinitionRegistry(ID)
    }
}
