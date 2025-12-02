package net.lixir.vminus.registry.tag

import net.lixir.vminus.Vminus.Companion.vminusIdentifier
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object VminusItemTags {
    val BANNED: TagKey<Item> = of("banned")
    val WOODEN_TOOLS: TagKey<Item> = of("tools/wooden")
    val STONE_TOOLS: TagKey<Item> = of("tools/stone")
    val GOLDEN_TOOLS: TagKey<Item> = of("tools/golden")
    val IRON_TOOLS: TagKey<Item> = of("tools/iron")
    val DIAMOND_TOOLS: TagKey<Item> = of("tools/diamond")
    val NETHERITE_TOOLS: TagKey<Item> = of("tools/netherite")
    val LEATHER_ARMORS: TagKey<Item> = of("armors/leather")
    val CHAINMAIL_ARMORS: TagKey<Item> = of("armors/chainmail")
    val GOLDEN_ARMORS: TagKey<Item> = of("armors/golden")
    val IRON_ARMORS: TagKey<Item> = of("armors/iron")
    val DIAMOND_ARMORS: TagKey<Item> = of("armors/diamond")
    val NETHERITE_ARMORS: TagKey<Item> = of("armors/netherite")
    val TURTLE_ARMORS: TagKey<Item> = of("armors/turtle")
    val GOLDEN_EQUIPMENT: TagKey<Item> = of("equipment/golden")
    val IRON_EQUIPMENT: TagKey<Item> = of("equipment/iron")
    val DIAMOND_EQUIPMENT: TagKey<Item> = of("equipment/diamond")
    val NETHERITE_EQUIPMENT: TagKey<Item> = of("equipment/netherite")
    val EQUIPMENT: TagKey<Item> = of("equipment")
    val BANNER_PATTERNS: TagKey<Item> = of("banner_patterns")
    val CONCRETE_POWDER: TagKey<Item> = of("concrete_powder")
    val GLAZED_TERRACOTTA: TagKey<Item> = of("glazed_terracotta")
    val CHEST_BOATS: TagKey<Item> = of("chest_boats")
    val BOATS: TagKey<Item> = of("boats")
    val DEAD_CORAL_BLOCKS: TagKey<Item> = of("dead_coral_blocks")
    val DEAD_CORALS: TagKey<Item> = of("dead_corals")
    val DEAD_CORAL_FANS: TagKey<Item> = of("dead_coral_fans")
    val WOODEN_FENCE_GATES: TagKey<Item> = of("wooden_fence_gates")
    val CREATIVE_ONLY: TagKey<Item> = of("creative_only")
    val NETHER_VINES: TagKey<Item> = of("nether_vines")

    private fun of(name: String): TagKey<Item> {
        return TagKey.of(RegistryKeys.ITEM, vminusIdentifier(name))
    }
}