package net.lixir.vminus.vision

import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagManagerLoader
import net.minecraft.util.Identifier

class VisionDataEntry<T> {
    val entries: MutableList<String> = mutableListOf()
    val values: MutableMap<String, List<VisionValue<*>>> = mutableMapOf()

    val isEmpty: Boolean
        get() = entries.isEmpty() || values.isEmpty()


    fun addValues(key: String, newValues: List<VisionValue<*>>) {
        values.merge(key, newValues) { existing, incoming ->
            existing + incoming
        }
    }

    fun addEntries(newEntries: List<String>) {
        entries.addAll(newEntries)
    }

    fun merge(other: VisionDataEntry<T>) {
        entries.addAll(other.entries)
        for ((key, incomingValues) in other.values) {
            values.merge(key, incomingValues) { existing, incoming ->
                existing + incoming
            }
        }
    }
    fun entryList(): List<String> = entries.toList()
    fun getValues(key: String): List<VisionValue<*>> = values[key] ?: emptyList()

    override fun toString(): String = buildString {
        append("VisionEntry {\n")
        append("  entries = $entries,\n")
        append("  values = {\n")
        values.forEach { (key, value) ->
            append("    $key = $value,\n")
        }
        append("  }\n")
        append("}")
    }

    companion object {
        fun visionApplies(
            obj: Any?,
            id: String,
            applicantList: List<String>,
            tagManagerLoader: TagManagerLoader
        ): Boolean {
            var invalidMatch = false
            var validMatchFound = false

            val idParts = id.split(":", limit = 2)
            val idPath = idParts.getOrElse(1) { idParts[0] }

            for (rawMatchKey in applicantList) {
                val inverted = rawMatchKey.startsWith("!")
                var matchKey = if (inverted) rawMatchKey.substring(1) else rawMatchKey

                val isTag = matchKey.startsWith("#")
                if (isTag) matchKey = matchKey.substring(1)

                val matchParts = matchKey.split(":", limit = 2)
                val matchNamespace = matchParts.getOrNull(0) ?: ""
                val matchPath = matchParts.getOrElse(1) { matchParts[0] }

                var found = matchKey == "all" ||
                        wildcardMatches(id, matchKey) ||
                        (matchNamespace.isEmpty() && (idPath == matchPath || wildcardMatches(idPath, matchPath))) ||
                        (!isTag && (id == matchKey || wildcardMatches(id, matchKey))) ||
                        (isTag && ((obj is Item && isItemTagged(obj, Identifier.tryParse(matchKey), tagManagerLoader)) ||
                                (obj is Block && isBlockTagged(obj, Identifier.tryParse(matchKey), tagManagerLoader)) ||
                                (obj is EntityType<*> && isEntityTagged(obj, Identifier.tryParse(matchKey), tagManagerLoader))
                                ))

                if (inverted) found = !found

                if (!inverted && found) validMatchFound = true
                else if (inverted && !found) {
                    invalidMatch = true
                    break
                }
            }

            return !invalidMatch && validMatchFound
        }

        private fun isItemTagged(item: Item, identifier: Identifier?, tagManagerLoader: TagManagerLoader): Boolean {
            if (identifier == null) return false
            return tagManagerLoader.isInTag<Item>(RegistryKeys.ITEM, identifier, item)
        }

        private fun isBlockTagged(block: Block, identifier: Identifier?, tagManagerLoader: TagManagerLoader): Boolean {
            if (identifier == null) return false
            return tagManagerLoader.isInTag<Block>(RegistryKeys.BLOCK, identifier, block)
        }

        private fun isEntityTagged(
            entityType: EntityType<*>,
            identifier: Identifier?,
            tagManagerLoader: TagManagerLoader
        ): Boolean {
            if (identifier == null) return false
            return tagManagerLoader.isInTag< EntityType<*>>(RegistryKeys.ENTITY_TYPE, identifier, entityType)
        }

        private fun wildcardMatches(value: String, pattern: String): Boolean {
            return when {
                pattern == "*" -> true
                pattern.startsWith("*") && pattern.endsWith("*") -> value.contains(pattern.substring(1, pattern.length - 1))
                pattern.startsWith("*") -> value.endsWith(pattern.substring(1))
                pattern.endsWith("*") -> value.startsWith(pattern.substring(0, pattern.length - 1))
                else -> value == pattern
            }
        }
    }
}
