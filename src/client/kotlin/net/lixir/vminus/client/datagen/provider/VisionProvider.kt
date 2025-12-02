package net.lixir.vminus.client.datagen.provider

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.lixir.vminus.extension.ArrayExtension.getByType
import net.lixir.vminus.item.ItemReplacement
import net.lixir.vminus.item.ItemStackSnapshot
import net.lixir.vminus.vision.VisionType
import net.lixir.vminus.vision.VisionTypes
import net.lixir.vminus.vision.property.BlockVisionProperties
import net.lixir.vminus.vision.property.ItemVisionProperties
import net.lixir.vminus.vision.property.VisionProperty
import net.minecraft.block.Block
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.jetbrains.annotations.Contract
import java.util.*
import java.util.concurrent.CompletableFuture

abstract class VisionProvider(
    private val output: FabricDataOutput,
    private val completableFuture: CompletableFuture<WrapperLookup>
) : DataProvider {

    val modId: String = output.modId
    private val visions: MutableMap<Identifier, VisionData<*>> = HashMap()

    protected abstract fun addVisions()

    private fun <T> register(visionData: VisionData<T>) {
        visions[Identifier.of(visionData.id.namespace,"/" + visionData.type.directory + "/" + visionData.id.path)] = visionData
    }
    fun itemVision(vararg keys: String): VisionData.Builder<Item> {
        return vision(VisionTypes.ITEM).isFor(*keys)
    }

    fun itemVision(vararg keys: RegistryKey<Item>): VisionData.Builder<Item> {
        return vision(VisionTypes.ITEM).isFor(*keys)
    }

    fun itemVision(vararg tags: TagKey<Item>): VisionData.Builder<Item> {
        return vision(VisionTypes.ITEM).isFor(*tags)
    }

    fun itemVision(vararg items: Item): VisionData.Builder<Item> {
        return vision(VisionTypes.ITEM).isFor(*items)
    }

    fun blockVision(vararg keys: RegistryKey<Block>): VisionData.Builder<Block> {
        return vision(VisionTypes.BLOCK).isFor(*keys)
    }
    
    fun blockVision(vararg tags: TagKey<Block>): VisionData.Builder<Block> {
        return vision(VisionTypes.BLOCK).isFor(*tags)
    }

    fun replaceItemWith(replace: Item, with: Item, path: String) {
        replaceItemWith(replace, with, Identifier.of(modId, path))
    }

    fun replaceItemWith(replaceTag: TagKey<Item>, withTag: TagKey<Item>, path: String) {
        replaceItemWith(replaceTag, withTag, Identifier.of(modId, path))
    }

    fun replaceItemWith(replaceTag: TagKey<Item>, withTag: TagKey<Item>, id: Identifier) {
        vision(VisionTypes.ITEM).isFor(replaceTag)
            .with(ItemVisionProperties.REPLACE, ItemReplacement(tag = withTag)).save(
                this,
                id
            )
    }

    fun replaceItemWith(replace: Item, withTag: TagKey<Item>, path: String) {
        replaceItemWith(replace, withTag, Identifier.of(modId, path))
    }

    fun replaceItemWith(replace: Item, withTag: TagKey<Item>, id: Identifier) {
        vision(VisionTypes.ITEM).isFor(replace)
            .with(ItemVisionProperties.REPLACE, ItemReplacement(tag = withTag)).save(
                this,
                id
            )
    }

    fun replaceItemWith(replace: Item, with: Item, id: Identifier) {
        vision(VisionTypes.ITEM).isFor(replace)
            .with(ItemVisionProperties.REPLACE, ItemReplacement(stackSnapshot = ItemStackSnapshot(with.defaultStack))).save(
                this,
                id
            )
    }

    fun replaceBlockWith(replace: Block, with: Block, path: String) {
        replaceBlockWith(replace, with, Identifier.of(modId, path))
    }

    fun replaceBlockWith(replace: Block, with: Block, id: Identifier) {
        vision(VisionTypes.BLOCK).isFor(replace).with(BlockVisionProperties.REPLACE, with).save(
            this,
            id
        )
    }

    fun blockVision(vararg blocks: Block): VisionData.Builder<Block> {
        return vision(VisionTypes.BLOCK).isFor(*blocks)
    }

    fun entityTypeVision(vararg keys: RegistryKey<EntityType<*>>): VisionData.Builder<EntityType<*>> {
        return vision(VisionTypes.ENTITY_TYPE).isFor(*keys)
    }
    
    fun entityTypeVision(vararg tags: TagKey<EntityType<*>>): VisionData.Builder<EntityType<*>> {
        return vision(VisionTypes.ENTITY_TYPE).isFor(*tags)
    }

    fun entityTypeVision(vararg entities: EntityType<*>): VisionData.Builder<EntityType<*>> {
        return vision(VisionTypes.ENTITY_TYPE).isFor(*entities)
    }

    fun statusEffectVision(vararg keys: RegistryKey<StatusEffect>): VisionData.Builder<StatusEffect> {
        return vision(VisionTypes.STATUS_EFFECT).isFor(*keys)
    }
    
    fun statusEffectVision(vararg tags: TagKey<StatusEffect>): VisionData.Builder<StatusEffect> {
        return vision(VisionTypes.STATUS_EFFECT).isFor(*tags)
    }

    fun statusEffectVision(vararg effects: StatusEffect): VisionData.Builder<StatusEffect> {
        return vision(VisionTypes.STATUS_EFFECT).isFor(*effects)
    }
    
    fun itemGroupVision(vararg keys: RegistryKey<ItemGroup>): VisionData.Builder<ItemGroup> {
        return vision(VisionTypes.ITEM_GROUP).isFor(*keys)
    }
    
    fun itemGroupVision(vararg tags: TagKey<ItemGroup>): VisionData.Builder<ItemGroup> {
        return vision(VisionTypes.ITEM_GROUP).isFor(*tags)
    }

    fun itemGroupVision(vararg groups: ItemGroup): VisionData.Builder<ItemGroup> {
        return vision(VisionTypes.ITEM_GROUP).isFor(*groups)
    }

    fun <T> vision(visionType: VisionType<T>): VisionData.Builder<T> {
        return VisionData.Builder.of(visionType)
    }

    override fun run(write: DataWriter): CompletableFuture<*> {
        addVisions()
        return CompletableFuture.allOf(
            *visions.entries.stream()
                .map { entry -> saveVision(write, entry.key, entry.value) }
                .toArray { arrayOfNulls<CompletableFuture<*>>(it) }
        )
    }

    private fun saveVision(writer: DataWriter, id: Identifier, builder: VisionData<*>): CompletableFuture<*> {
        val locationNamespace = id.namespace
        val locationPath = id.path
        val path =
            output.path.resolve("data/$locationNamespace$locationPath.json")

        val json = builder.toJson()
        return DataProvider.writeToPath(writer, json, path)
    }

    override fun getName(): String {
        return "Vision Provider [$modId]"
    }


    class VisionData<T> private constructor(
        builder: Builder<T>,
        val id: Identifier
    ) {
        val type = builder.type
        private val appliesTo: List<String> = builder.appliesTo
        private val properties: Map<VisionProperty<*>, MutableList<JsonElement>> = builder.properties

        fun toJson(): JsonObject {
            val jsonObject = JsonObject()

            val appliesArray = JsonArray()
            for (applicant in appliesTo) {
                appliesArray.add(applicant)
            }
            jsonObject.add(type.multiList, appliesArray)

            for ((key, values) in properties) {
                if (key.listable)
                    jsonObject.add(key.id, JsonArray().also { jsonArray ->
                        values.forEach { value ->
                            jsonArray.add(value)
                        }
                    })
                else
                    values.forEach { value ->
                        jsonObject.add(key.id, value)
                    }
            }

            return jsonObject
        }

        class Builder<T> private constructor(internal val type: VisionType<T>) {
            val appliesTo: MutableList<String> = ArrayList()
            val properties: MutableMap<VisionProperty<*>, MutableList<JsonElement>> = HashMap()

            fun <V> with(visionProperty: VisionProperty<V>, vararg values: V): Builder<T> {
                for (value: V in values) {
                    val element = visionProperty.codec.encodeStart(JsonOps.INSTANCE, value)
                        .result()
                        .orElseThrow { IllegalStateException("Failed to encode ${visionProperty.id}") }

                    properties.computeIfAbsent(visionProperty) { mutableListOf() }.add(element)
                }
                return this
            }


            fun isNotFor(vararg applicants: TagKey<T>): Builder<T> {
                for (applicant in applicants) appliesTo.add("!#" + applicant.id())
                return this
            }

            fun isFor(vararg applicants: TagKey<T>): Builder<T> {
                for (applicant in applicants) appliesTo.add("#" + applicant.id())
                return this
            }

            fun isNotFor(vararg applicants: RegistryKey<T>): Builder<T> {
                for (applicant in applicants) appliesTo.add("!" + applicant.value.toString())
                return this
            }

            fun isFor(vararg applicants: RegistryKey<T>): Builder<T> {
                for (applicant in applicants) appliesTo.add(applicant.value.toString())
                return this
            }

            @Suppress("UNCHECKED_CAST")
            fun isFor(vararg applicants: T): Builder<T> {
                val raw = type.registry as Registry<T>
                for (applicant in applicants) {
                    val id = raw.getId(applicant)
                    requireNotNull(id) { "No registry key found for $applicant" }
                    appliesTo.add(id.toString())
                }
                return this
            }

            @Suppress("UNCHECKED_CAST")
            fun isNotFor(vararg applicants: T): Builder<T> {
                val raw = type.registry as Registry<T>
                for (applicant in applicants) {
                    val id = raw.getId(applicant)
                    requireNotNull(id) { "No registry key found for $applicant" }
                    appliesTo.add("!$id")
                }
                return this
            }

            fun isFor(vararg appliesTo: String): Builder<T> {
                this.appliesTo.addAll(appliesTo.toList())
                return this
            }

            fun isNotFor(vararg appliesTo: String): Builder<T> {
                val mutable = appliesTo.toMutableList()

                for (i in mutable.indices) {
                    mutable[i] = "!" + mutable[i]
                }

                this.appliesTo.addAll(mutable)
                return this
            }

            fun save(visionProvider: VisionProvider, location: String): VisionData<*> {
                require(appliesTo.isNotEmpty()) { "Vision must have at least 1 applicant defined." }
                require(properties.isNotEmpty()) { "Vision must have at least 1 property defined." }
                val visionData: VisionData<*> = VisionData(this, Identifier.of(visionProvider.modId, location))
                visionProvider.register(visionData)
                return visionData
            }

            fun save(visionProvider: VisionProvider, id: Identifier): VisionData<*> {
                require(appliesTo.isNotEmpty()) { "Vision must have at least 1 applicant defined." }
                require(properties.isNotEmpty()) { "Vision must have at least 1 property defined." }
                val visionData: VisionData<*> = VisionData(this, id)
                visionProvider.register(visionData)
                return visionData
            }

            companion object {
                @Contract("_ -> new")
                fun <T> of(type: VisionType<T>): Builder<T> {
                    return Builder(type)
                }
            }
        }
    }
}
