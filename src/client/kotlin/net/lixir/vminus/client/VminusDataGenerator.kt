package net.lixir.vminus.client

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.loader.api.FabricLoader
import net.lixir.vminus.Vminus
import net.lixir.vminus.Vminus.Companion.ID
import net.lixir.vminus.Vminus.Companion.REGISTRY
import net.lixir.vminus.client.datagen.*
import net.lixir.vminus.client.datagen.provider.*
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionRegistry
import net.lixir.vminus.client.definition.datagen.VminusDataGenDefinitionGroupProvider
import net.lixir.vminus.registry.VRegistry

class VminusDataGenerator : VDataGenerator(), DataGeneratorEntrypoint {

    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        if (!System.getProperty("vminus.datagen", "false").toBoolean()) return
        super.onInitializeDataGenerator(fabricDataGenerator)
        pack.addProvider(::VminusItemTagProvider)
        pack.addProvider(::VminusBlockTagProvider)
        pack.addProvider(::VminusModelProvider)
        pack.addProvider(::VminusLanguageProvider)
        pack.addProvider(::VminusEntityTypeTagProvider)
        pack.addProvider(::VminusVisionProvider)
    }

    override val vRegistry = REGISTRY

    override val definitionRegistry = DATAGEN_DEFINITION_REGISTRY

    companion object {
        val DATAGEN_DEFINITION_REGISTRY = DataGenDefinitionRegistry(ID, VminusDataGenDefinitionGroupProvider())
    }
}
