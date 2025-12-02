package net.lixir.vminus.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

object VisionFormatter {
    @Throws(JsonParseException::class)
    fun processJson(singleListName: String, multiListName: String, jsonFile: JsonElement): JsonObject {
        val jsonFileObject = jsonFile.asJsonObject

        if (!jsonFileObject.has(multiListName)) jsonFileObject.add(multiListName, JsonArray())
        // Wrap single keys if they exist into lists for further formatting
        keyToArray(jsonFileObject, singleListName, multiListName)
        keyToArray(jsonFileObject, "tag", multiListName)

        val processedJsonObject = JsonObject()

        // Add the list back if it existed
        if (jsonFileObject.has(multiListName) && jsonFileObject[multiListName].isJsonArray) processedJsonObject.add(
            multiListName,
            jsonFileObject[multiListName]
        )

        for ((key, jsonElement) in jsonFileObject.entrySet()) {
            if ((jsonElement.isJsonObject || jsonElement.isJsonPrimitive || jsonElement.isJsonArray)
                && key != singleListName && key != multiListName) {
                processedJsonObject.add(key, jsonElement)
            }
        }

        return processedJsonObject
    }

    private fun wrapObjectInArray(jsonElement: JsonElement, mergedConditions: JsonArray): JsonArray {
        val jsonObject = jsonElement.asJsonObject
        val newArray = JsonArray()
        val newObject = JsonObject()
        for ((entryKey, entryElement) in jsonObject.entrySet()) {
            newObject.add(entryKey, entryElement)
        }
        newObject.add("conditions", mergedConditions)
        newArray.add(newObject)
        return newArray
    }

    fun keyToArray(jsonFileObject: JsonObject, singleName: String, listType: String) {
        if (!jsonFileObject.has(singleName)) return

        val singleElement = jsonFileObject[singleName]

        if (!singleElement.isJsonPrimitive) return

        var singleValue = singleElement.asString
        if (singleName == "tag") {
            if (!singleValue.startsWith("#")) {
                singleValue = "#$singleValue"
            }
        }

        val jsonArray: JsonArray

        if (jsonFileObject.has(listType) && jsonFileObject[listType].isJsonArray) {
            jsonArray = jsonFileObject[listType].asJsonArray
        } else {
            jsonArray = JsonArray()
            jsonFileObject.add(listType, jsonArray)
        }

        jsonFileObject.remove(singleName)
        jsonArray.add(singleValue)
    }


    @Throws(JsonParseException::class)
    fun getEntries(listName: String, jsonObject: JsonObject): ArrayList<String> {
        val listArray: JsonArray
        if (jsonObject.has(listName)) {
            if (jsonObject[listName].isJsonArray) {
                listArray = jsonObject.getAsJsonArray(listName)
            } else {
                throw JsonParseException("$listName is not a JsonArray.")
            }
        } else {
            throw JsonParseException("$listName not found. $jsonObject")
        }
        return ArrayList(
            listArray.asList().stream()
                .map { obj: JsonElement -> obj.asString }
                .toList())
    }
}
