package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.lixir.vminus.Vminus.Companion.devLogger
import net.lixir.vminus.client.definition.datagen.BlockDataGenDefinitionEntry
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionRegistry
import net.lixir.vminus.client.definition.datagen.ItemDataGenDefinitionEntry
import net.minecraft.data.client.*
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import java.util.*

abstract class VModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    protected val modId: String = output.modId

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        val registry = DataGenDefinitionRegistry.fromId(modId) ?: return
        for ((block, definition) in registry.blockDefinitions.entries) {
            val modelType = definition.modelType
            if (modelType.isEmpty) continue
            modelType.apply(block, this, blockStateModelGenerator)
            devLogger("Generated $modelType for $block")
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        val registry = DataGenDefinitionRegistry.fromId(modId) ?: return
        for ((item, definition) in registry.itemDefinitions.entries) {
            val modelType = definition.modelType
            if (modelType.isEmpty) continue
            modelType.apply(item, this, itemModelGenerator)
            devLogger("Generated $modelType for $item")
        }
    }

    fun cubeAll(entry: BlockDataGenDefinitionEntry, generator: BlockStateModelGenerator) {
        val block = entry.block
        generator.registerSimpleCubeAll(block)
    }

    fun parentBlock(entry: ItemDataGenDefinitionEntry, generator: ItemModelGenerator) {
        val item = entry.item
        val parent = Identifier.of(modId, "block/" + Registries.ITEM.getId(item).path)
        val model = Model(Optional.of(parent), Optional.empty())
        generator.register(item, model)
    }

    fun basic(entry: ItemDataGenDefinitionEntry, generator: ItemModelGenerator) {
        val item = entry.item
        generator.register(item, Models.GENERATED)
    }

    fun handheld(entry: ItemDataGenDefinitionEntry, generator: ItemModelGenerator) {
        val item = entry.item
        generator.register(item, Models.HANDHELD)
    }

    fun pane(entry: ItemDataGenDefinitionEntry, generator: ItemModelGenerator) {
        val item = entry.item
        val texture = Identifier.of(modId, "block/" + Registries.ITEM.getId(item).path)
        Models.GENERATED.upload(ModelIds.getItemModelId(item), TextureMap.layer0(texture), generator.writer)
    }
}
