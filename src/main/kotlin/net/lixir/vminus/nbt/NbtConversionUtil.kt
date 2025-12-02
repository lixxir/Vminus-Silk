package net.lixir.vminus.nbt

import com.google.gson.*
import net.minecraft.nbt.*

object NbtConversionUtil {
    fun compoundToJson(nbt: NbtCompound): JsonObject = JsonObject().apply {
        nbt.keys.forEach { key ->
            when (val element = nbt[key]) {
                is NbtCompound -> add(key, compoundToJson(element))
                is NbtList -> add(key, JsonArray().apply {
                    element.forEach { item ->
                        when (item) {
                            is NbtCompound -> add(compoundToJson(item))
                            is NbtString -> add(JsonPrimitive(item.asString()))
                            else -> add(JsonPrimitive(item.asString()))
                        }
                    }
                })
                is NbtInt -> addProperty(key, element.intValue())
                is NbtByte -> {
                    when (val v = element.byteValue().toInt()) {
                        0 -> addProperty(key, false)
                        1 -> addProperty(key, true)
                        else -> addProperty(key, v)
                    }
                }
                is NbtShort -> addProperty(key, element.shortValue())
                is NbtLong -> addProperty(key, element.longValue())
                is NbtFloat -> addProperty(key, element.floatValue())
                is NbtDouble -> addProperty(key, element.doubleValue())
                is NbtString -> addProperty(key, element.asString())
                else -> addProperty(key, element?.asString() ?: "null")
            }
        }
    }

    fun jsonToCompound(json: JsonObject): NbtCompound = NbtCompound().apply {
        json.entrySet().forEach { (key, value) ->
            when {
                value.isJsonPrimitive -> {
                    val primitive = value.asJsonPrimitive
                    when {
                        primitive.isBoolean -> putBoolean(key, primitive.asBoolean)
                        primitive.isNumber -> {
                            val num = primitive.asNumber
                            val str = primitive.asString
                            when {
                                str.endsWith("b", true) -> putByte(key, num.toByte())
                                str.endsWith("s", true) -> putShort(key, num.toShort())
                                str.endsWith("f", true) -> putFloat(key, num.toFloat())
                                str.endsWith("l", true) -> putLong(key, num.toLong())
                                num.toDouble() == num.toInt().toDouble() -> putInt(key, num.toInt())
                                else -> putDouble(key, num.toDouble())
                            }
                        }
                        else -> putString(key, primitive.asString)
                    }
                }
                value.isJsonObject -> put(key, jsonToCompound(value.asJsonObject))
                value.isJsonArray -> put(key, NbtList().apply {
                    value.asJsonArray.forEach { e ->
                        add(
                            when {
                                e.isJsonPrimitive -> NbtString.of(e.asJsonPrimitive.asString)
                                e.isJsonObject -> jsonToCompound(e.asJsonObject)
                                else -> NbtString.of(e.toString())
                            }
                        )
                    }
                })
            }
        }
    }
}
