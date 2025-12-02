package net.lixir.vminus.serialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import net.lixir.vminus.vision.VisionDataEntry
import net.lixir.vminus.vision.VisionType
import net.lixir.vminus.vision.property.VisionPropertyRegistry.fromVisionType
import java.lang.reflect.Type

import com.mojang.serialization.JsonOps
import net.lixir.vminus.Vminus
import net.lixir.vminus.vision.VisionValue

class VisionDeserializer<T>(
    private val listName: String,
    private val visionType: VisionType<T>
) : JsonDeserializer<VisionDataEntry<T>> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        deserializationContext: JsonDeserializationContext
    ): VisionDataEntry<T> {
        val jsonObject = json.asJsonObject
        val visionEntry = VisionDataEntry<T>().apply {
            addEntries(VisionFormatter.getEntries(listName, jsonObject))
        }

        val visionProperties = fromVisionType(visionType)

        visionProperties.forEach { visionProperty ->
            val id = visionProperty.id
            if (jsonObject.has(id)) {
                val propertyCodec = visionProperty.codec
                val jsonElement = jsonObject.get(id)
                    ?: throw JsonParseException("Expected a JSON Element for id: $id")

                try {
                    val parsedValues: List<Any?> = if (visionProperty.listable) {
                        // For listable properties, create one VisionValue per element
                        jsonElement.asJsonArray.mapNotNull { element ->
                            val result = propertyCodec.parse(JsonOps.INSTANCE, element)
                            result.error().ifPresent { err ->
                                Vminus.LOGGER.warn("Codec error for $id element: $err")
                            }
                            result.result().orElse(null)
                        }
                    } else {
                        // Single-value properties
                        val result = propertyCodec.parse(JsonOps.INSTANCE, jsonElement)
                        result.error().ifPresent { err ->
                            Vminus.LOGGER.warn("Codec error for $id: $err")
                        }
                        listOf(result.result().orElse(null))
                    }

                    if (parsedValues.isNotEmpty()) {
                        val visionValues = if (visionProperty.listable) {
                            parsedValues.filterNotNull().map { VisionValue(values = mutableListOf(it)) }.toMutableList()
                        } else {
                            mutableListOf(VisionValue(values = parsedValues.toMutableList()))
                        }
                        visionEntry.addValues(id, visionValues)
                    }


                } catch (ex: Exception) {
                    Vminus.LOGGER.error("Exception parsing property $id: $jsonElement", ex)
                }
            }
        }

        return visionEntry
    }
}
