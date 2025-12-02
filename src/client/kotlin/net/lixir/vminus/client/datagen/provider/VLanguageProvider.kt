package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.lixir.vminus.client.definition.datagen.*
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import java.util.concurrent.CompletableFuture

abstract class VLanguageProvider protected constructor(
    dataOutput: FabricDataOutput,
    languageCode: String = "en_us",
    registryLookup: CompletableFuture<WrapperLookup>
) : FabricLanguageProvider(
    dataOutput,
    languageCode,
    registryLookup
) {
    protected val modId: String = dataOutput.modId

    override fun generateTranslations(wrapperLookup: WrapperLookup, translationBuilder: TranslationBuilder) {
        val registry = DataGenDefinitionRegistry.fromId(modId) ?: return

        for ((item, definition) in registry.itemDefinitions.entries) {
            autoGenerate(
                item,
                Registries.ITEM.getId(item).path,
                definition,
                translationBuilder
            )
        }

        for ((block, definition) in registry.blockDefinitions.entries) {
            autoGenerate(
                block,
                Registries.BLOCK.getId(block).path,
                definition,
                translationBuilder
            )
        }
        for ((entityType, definition) in registry.entityTypeDefinitions.entries) {
            autoGenerate(
                entityType,
                Registries.ENTITY_TYPE.getId(entityType).path,
                definition,
                translationBuilder
            )
        }
    }

    protected fun <E : DataGenDefinition<E, T>, T> autoGenerate(
        any: Any?,
        idPath: String,
        definition: DataGenDefinition<E, T>,
        translationBuilder: TranslationBuilder
    ) {
        val langKey = definition.langKey
        if (langKey.isEmpty) return
        if (langKey.isUnset) {
            val name = toTitleCase(idPath.replace('_', ' '))
            when (any) {
                is Block -> translationBuilder.add(any, name)
                is Item -> translationBuilder.add(any, name)
                is EntityType<*> -> translationBuilder.add(any, name)
            }
        } else {
            val lang = langKey.key
            when (any) {
                is Block -> translationBuilder.add(any, lang)
                is Item -> translationBuilder.add(any, lang)
                is EntityType<*> -> translationBuilder.add(any, lang)
            }
        }
    }

    companion object {
        private fun toTitleCase(input: String): String {
            val words = input.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val sb = StringBuilder()
            for (word in words) {
                if (word.isNotEmpty()) {
                    sb.append(word[0].uppercaseChar())
                        .append(word.substring(1))
                        .append(" ")
                }
            }
            return sb.toString().trim { it <= ' ' }
        }
    }
}
