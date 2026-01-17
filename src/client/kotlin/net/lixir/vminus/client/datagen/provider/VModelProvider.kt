package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.lixir.vminus.Vminus.Companion.devLogger
import net.lixir.vminus.block.VminusBlockExtensions.getIdentifier
import net.lixir.vminus.client.definition.Definition.Companion.IDENTIFIER_UNSET
import net.lixir.vminus.client.definition.Definition.Companion.STRING_UNSET
import net.lixir.vminus.client.definition.datagen.BlockDatagenDefinition
import net.lixir.vminus.client.definition.datagen.DatagenDefinitionRegistry
import net.lixir.vminus.client.definition.datagen.DefinitionTextureSettings
import net.lixir.vminus.client.definition.datagen.ItemDatagenDefinition
import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import java.util.*

abstract class VModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    protected val modId: String = output.modId

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        val registry = DatagenDefinitionRegistry.fromId(modId) ?: return
        for ((block, definition) in registry.blockDefinitions.entries) {
            val modelType = definition.modelType
            if (modelType.isEmpty) continue
            modelType.action(block, definition, this, blockStateModelGenerator)
            devLogger("Generated $modelType for $block")
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        val registry = DatagenDefinitionRegistry.fromId(modId) ?: return
        for ((item, definition) in registry.itemDefinitions.entries) {
            val modelType = definition.modelType
            if (modelType.isEmpty) continue
            modelType.action(item, definition, this, itemModelGenerator)
            devLogger("Generated $modelType for $item")
        }
    }

    fun generatedItem(
        item: Item,
        definition: ItemDatagenDefinition,
        generator: ItemModelGenerator
    ) {
        generator.register(item, Models.GENERATED)
    }

    fun cubeAll(
        block: Block,
        definition: BlockDatagenDefinition,
        generator: BlockStateModelGenerator
    ) {
        generator.registerSimpleCubeAll(block)
    }

    fun parentBlock(
        item: Item,
        definition: ItemDatagenDefinition,
        generator: ItemModelGenerator
    ) {
        val parent = Identifier.of(modId, "block/" + Registries.ITEM.getId(item).path)
        val model = Model(Optional.of(parent), Optional.empty())
        generator.register(item, model)
    }

    fun handheldItem(
        item: Item,
        definition: ItemDatagenDefinition,
        generator: ItemModelGenerator
    ) {
        generator.register(item, Models.HANDHELD)
    }

    fun paneItem(
        item: Item,
        definition: ItemDatagenDefinition,
        generator: ItemModelGenerator
    ) {
        val texture = Identifier.of(modId, "block/" + Registries.ITEM.getId(item).path)
        Models.GENERATED.upload(ModelIds.getItemModelId(item), TextureMap.layer0(texture), generator.writer)
    }

    fun definitionIdentifier(identifier: Identifier, settings: DefinitionTextureSettings): Identifier {
        val override = settings.override
        if (override != IDENTIFIER_UNSET)
            return override

        val removeSuffix = settings.removeSuffix
        val addSuffix = settings.addSuffix

        val namespace = identifier.namespace
        var path = identifier.path

        if (removeSuffix != STRING_UNSET && removeSuffix.isNotEmpty() && path.endsWith(removeSuffix)) {
            path = path.removeSuffix(removeSuffix)
        }

        if (removeSuffix != addSuffix && addSuffix.isNotEmpty()) {
            path += addSuffix
        }

        return Identifier.of(namespace, path)
    }

    fun textureFromBlock(block: Block, definition: BlockDatagenDefinition, addSuffix: String = "", remove: String = ""): Identifier {
        var identifier = block.getIdentifier()
        val namespace = identifier.namespace
        val path = identifier.path.replace(remove, "")
        val settings = definition.textureSettings
        identifier = Identifier.of(namespace, "block/$path$addSuffix")
        identifier = definitionIdentifier(identifier, settings)
        return identifier
    }
}
