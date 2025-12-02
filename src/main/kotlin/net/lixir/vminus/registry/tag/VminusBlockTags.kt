package net.lixir.vminus.registry.tag

import net.lixir.vminus.Vminus.Companion.vminusIdentifier
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object VminusBlockTags {
    val WOODEN_FENCE_GATES: TagKey<Block> = of("wooden_fence_gates")
    val REDSTONE_TORCHES: TagKey<Block> = of("redstone_torches")
    val SOUL_TORCHES: TagKey<Block> = of("soul_torches")
    val TORCHES: TagKey<Block> = of("torches")
    val ALL_TORCHES: TagKey<Block> = of("all_torches")
    val FROGLIGHTS: TagKey<Block> = of("froglights")
    val MOB_HEADS: TagKey<Block> = of("mob_heads")
    val CONCRETE_POWDER: TagKey<Block> = of("concrete_powder")
    val BRUSHABLE: TagKey<Block> = of("brushable")
    val CAN_SUSTAIN_PLANTS: TagKey<Block> = of("can_sustain_plants")
    val CAN_SUSTAIN_CACTUS: TagKey<Block> = of("can_sustain_cactus")
    val CAN_SUSTAIN_DEAD_BUSH: TagKey<Block> = of("can_sustain_dead_bush")
    val DYEABLE: TagKey<Block> = of("dyeable")
    val DYED: TagKey<Block> = of("dyed")
    val DYED_WOOL: TagKey<Block> = of("dyed/wool")
    val DYED_TERRACOTTA: TagKey<Block> = of("dyed/terracotta")
    val DYED_CONCRETE: TagKey<Block> = of("dyed/concrete")
    val DYED_BEDS: TagKey<Block> = of("dyed/beds")
    val DYED_BANNERS: TagKey<Block> = of("dyed/banners")
    val DYED_CANDLES: TagKey<Block> = of("dyed/candles")
    val DYED_GLAZED_TERRACOTTA: TagKey<Block> = of("dyed/glazed_terracotta")
    val DYED_CARPETS: TagKey<Block> = of("dyed/carpets")
    val DYED_CONCRETE_POWDER: TagKey<Block> = of("dyed/concrete_powder")
    val DYED_STAINED_GLASS: TagKey<Block> = of("dyed/stained_glass")
    val DYED_STAINED_GLASS_PANE: TagKey<Block> = of("dyed/stained_glass_pane")
    val GRASSES: TagKey<Block> = of("grasses")
    val TALL_GRASSES: TagKey<Block> = of("tall_grasses")
    val LEASHABLE: TagKey<Block> = of("leashable")
    val COCAO_PLANTABLE_ON: TagKey<Block> = of("cocao_plantable_on")
    val DEAD_CORAL_BLOCKS: TagKey<Block> = of("dead_coral_blocks")
    val DEAD_CORALS: TagKey<Block> = of("dead_corals")
    val DEAD_CORAL_FANS: TagKey<Block> = of("dead_coral_fans")
    val MUSHROOMS: TagKey<Block> = of("mushrooms")
    val NETHER_FUNGUS: TagKey<Block> = of("nether_fungus")
    val NETHER_VINES: TagKey<Block> = of("nether_vines")
    val VALID_SPAWN: TagKey<Block> = of("valid_spawn")

    private fun of(name: String): TagKey<Block> {
        return TagKey.of(RegistryKeys.BLOCK, vminusIdentifier(name))
    }
}