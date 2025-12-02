package net.lixir.vminus.client.datagen.provider

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import lombok.EqualsAndHashCode
import lombok.ToString
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.lixir.vminus.Vminus.Companion.devLogger
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.CompletableFuture

abstract class SoundDefinitionProvider(protected val output: FabricDataOutput) : DataProvider {
    private val modId: String = output.modId
    private val soundEntries: MutableMap<String, SoundEntry> = LinkedHashMap()

    protected abstract fun generateSounds(output: FabricDataOutput?)

    protected fun add(
        event: SoundEvent,
        subtitle: String? = null,
        replace: Boolean = false,
        count: Int = 1,
        vararg soundLocations: Any // either String paths or SoundVariant
    ): SoundEntry {
        val entry = SoundEntry(subtitle, replace)
        for (loc in soundLocations) {
            when (loc) {
                is String -> {
                    for (i in 1..count) {
                        var finalName = if (count > 1) appendIndexBeforeExtension(loc, i) else loc
                        if (!finalName.contains(":")) finalName = "$modId:$finalName"
                        entry.sounds.add(SoundVariant(Identifier.tryParse(finalName)))
                    }
                }

                is SoundVariant -> entry.sounds.add(loc)
                else -> throw IllegalArgumentException("Unsupported sound variant type: $loc")
            }
        }

        soundEntries[event.id.path] = entry
        return entry
    }

    override fun run(writer: DataWriter): CompletableFuture<*> {
        generateSounds(output)
        val root = JsonObject()
        for ((key, value) in soundEntries) {
            devLogger("Generating sound entry $key of $value")
            root.add(key, value.toJson())
        }

        val path = output.path.resolve("assets").resolve(modId).resolve("sounds.json")
        devLogger("Writing sounds.json to: ${path.toAbsolutePath()}")

        return DataProvider.writeToPath(writer, root, path)
    }

    override fun getName(): String = "Sound Definitions"

    class SoundEntry(
        private var subtitle: String? = null,
        private var replace: Boolean = false,
        val sounds: MutableList<SoundVariant> = ArrayList()
    ) {
        fun toJson(): JsonObject {
            val obj = JsonObject()
            subtitle?.let { obj.addProperty("key", it) }
            if (replace) obj.addProperty("replace", true)
            val arr = JsonArray()
            sounds.forEach { arr.add(it.toJson()) }
            obj.add("sounds", arr)
            return obj
        }
    }


    class SoundVariant(val identifier: Identifier?) {
        private var stream: Boolean = false
        private var volume: Float = 1.0f
        private var pitch: Float = 1.0f

        fun toJson(): JsonObject {
            val obj = JsonObject()
            obj.addProperty("name", identifier.toString())
            if (stream) obj.addProperty("stream", true)
            if (volume != 1.0f) obj.addProperty("volume", volume)
            if (pitch != 1.0f) obj.addProperty("pitch", pitch)
            return obj
        }
    }

    companion object {
        private fun appendIndexBeforeExtension(path: String, index: Int): String {
            val dotIndex = path.lastIndexOf('.')
            return if (dotIndex == -1 || dotIndex == path.length - 1) {
                path + index
            } else {
                path.substring(0, dotIndex) + index + path.substring(dotIndex)
            }
        }
    }
}

