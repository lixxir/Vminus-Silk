package net.lixir.vminus.vision.condition

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive

abstract class AbstractVisionCondition(val isInverted: Boolean) {
    abstract fun test(visionContext: VisionContext?): Boolean

    companion object {
        @Throws(JsonParseException::class)
        fun parseVisionConditions(
            arrayObject: JsonObject?,
            jsonObject: JsonObject?
        ): List<List<AbstractVisionCondition>> {
            return emptyList()
        }

        @Throws(JsonParseException::class)
        private fun getConditionValue(conditionObject: JsonObject): JsonPrimitive {
            return getConditionValue("value", conditionObject)
        }

        @Throws(JsonParseException::class)
        private fun getConditionValue(valueName: String, conditionObject: JsonObject): JsonPrimitive {
            if (!conditionObject.has(valueName)) throw JsonParseException("$conditionObject has no defined value string.")
            return conditionObject.getAsJsonPrimitive(valueName)
        }
    }
}
