package net.lixir.vminus.vision

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import net.lixir.vminus.Vminus
import net.lixir.vminus.serialization.VisionDeserializer
import net.lixir.vminus.serialization.VisionFormatter
import net.lixir.vminus.duck.VisionDuck
import net.lixir.vminus.mixin.resource.JsonDataLoaderAccessor
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagManagerLoader
import net.minecraft.resource.JsonDataLoader
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler

class VisionManagerLoader<T : Any>(
    val visionType: VisionType<T>,
    private val registry: Registry<T>,
    private val tagManagerLoader: TagManagerLoader
) : JsonDataLoader(
    GsonBuilder()
        .registerTypeAdapter(VisionDataEntry::class.java,
            VisionDeserializer(visionType.multiList, visionType)
        )
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create(),
    visionType.directory
) {

    private val idToVisionMap: MutableMap<Identifier, Vision> = mutableMapOf()
    private val gson: Gson = (this as JsonDataLoaderAccessor).gson
    private val singleListName = visionType.id
    private val multiListName = visionType.multiList

    init {
        visionManagerLoaders.add(this)
    }

    override fun apply(
        resourceLocationJsonElementMap: Map<Identifier, JsonElement>,
        resourceManager: ResourceManager,
        profiler: Profiler
    ) {
        clearMap()
        val visionEntries = mutableListOf<VisionDataEntry<T>>()

        // Load from data packs
        resourceLocationJsonElementMap.forEach { (key, value) ->
            loadVisionEntry(key.toString(), value, visionEntries)
        }

        if (visionEntries.isNotEmpty()) {
            Vminus.LOGGER.info("Loaded ${visionEntries.size} ${visionType.id} visions from data")
        }

        // Apply visions to registry entries
        registry.forEach { value ->
            val id = registry.getId(value) ?: return@forEach
            val duck = value as VisionDuck
            duck.`vminus$setVisionIdentifier`(id)
            val mergedEntry = buildMergedEntry(value, id.toString(), visionEntries)

            if (!mergedEntry.isEmpty) {
                val vision = Vision.fromEntry(id, mergedEntry, visionType)
                idToVisionMap[id] = vision
                visionType.putVision(id, vision)
                visionType.applyVision(value, id)
            }
        }
    }

    private fun clearMap() = idToVisionMap.clear()

    private fun buildMergedEntry(value: T, id: String, visionEntries: List<VisionDataEntry<T>>): VisionDataEntry<T> {
        val mergedEntry = VisionDataEntry<T>()
        visionEntries.forEach { entry ->
            if (VisionDataEntry.visionApplies(value, id, entry.entries, tagManagerLoader)) {
                mergedEntry.merge(entry)
            }
        }
        return mergedEntry
    }

    private fun loadVisionEntry(source: String, element: JsonElement, outputList: MutableList<VisionDataEntry<T>>) {
        try {
            val processed = VisionFormatter.processJson(singleListName, multiListName, element)
            val entry = gson.fromJson<VisionDataEntry<T>>(
                processed,
                object : TypeToken<VisionDataEntry<T>>() {}.type
            )
            outputList.add(entry)
        } catch (e: Exception) {
            Vminus.LOGGER.error("Failed to load VisionEntry from '{}': {}", source, e.message)
        }
    }

    val visionEntries: Set<Map.Entry<Identifier, Vision>>
        get() = idToVisionMap.entries

    override fun hashCode(): Int = visionType.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VisionManagerLoader<*>) return false

        return idToVisionMap == other.idToVisionMap &&
                singleListName == other.singleListName &&
                multiListName == other.multiListName &&
                gson == other.gson &&
                visionType == other.visionType &&
                registry == other.registry &&
                tagManagerLoader == other.tagManagerLoader
    }

    override fun toString(): String = "VisionManager[$singleListName]"

    companion object {
        val visionManagerLoaders: MutableSet<VisionManagerLoader<*>> = mutableSetOf()

        fun clearVisionManagers() {
            visionManagerLoaders.clear()
        }
    }
}
