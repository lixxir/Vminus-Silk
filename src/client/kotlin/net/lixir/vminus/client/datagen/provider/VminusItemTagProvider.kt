package net.lixir.vminus.client.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.lixir.vminus.extension.data.tag.DetourFabricTagBuilderExtension.addOptionalTags
import net.lixir.vminus.registry.tag.VminusItemTags
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import java.util.concurrent.CompletableFuture

class VminusItemTagProvider(output: FabricDataOutput, completableFuture: CompletableFuture<WrapperLookup>) :
    VItemTagProvider(output, completableFuture) {

    override fun configure(arg: WrapperLookup) {
        super.configure(arg)
        getOrCreateTagBuilder(VminusItemTags.BANNED)
        getOrCreateTagBuilder(VminusItemTags.EQUIPMENT).apply {
            addOptionalTags(
                ConventionalItemTags.ARMORS,
                ConventionalItemTags.TOOLS
            )
        }
        getOrCreateTagBuilder(ConventionalItemTags.ARMORS).apply {
            addOptionalTags(
                VminusItemTags.IRON_ARMORS,
                VminusItemTags.DIAMOND_ARMORS,
                VminusItemTags.GOLDEN_ARMORS,
                VminusItemTags.LEATHER_ARMORS,
                VminusItemTags.CHAINMAIL_ARMORS,
                VminusItemTags.TURTLE_ARMORS,
                VminusItemTags.NETHERITE_ARMORS
            )
        }
        getOrCreateTagBuilder(VminusItemTags.TURTLE_ARMORS).apply {
            add(
                Items.TURTLE_HELMET,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.CHAINMAIL_ARMORS).apply {
            add(
                Items.CHAINMAIL_HELMET,
                Items.CHAINMAIL_CHESTPLATE,
                Items.CHAINMAIL_LEGGINGS,
                Items.CHAINMAIL_BOOTS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.LEATHER_ARMORS).apply {
            add(
                Items.LEATHER_HELMET,
                Items.LEATHER_CHESTPLATE,
                Items.LEATHER_LEGGINGS,
                Items.LEATHER_BOOTS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.WOODEN_TOOLS).apply {
           add(
               Items.WOODEN_PICKAXE,
               Items.WOODEN_SWORD,
               Items.WOODEN_AXE,
               Items.WOODEN_HOE,
               Items.WOODEN_SHOVEL
           )
        }
        getOrCreateTagBuilder(VminusItemTags.DIAMOND_EQUIPMENT).apply {
            addOptionalTags(
                VminusItemTags.DIAMOND_TOOLS,
                VminusItemTags.DIAMOND_ARMORS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.DIAMOND_ARMORS).apply {
            add(
                Items.DIAMOND_HELMET,
                Items.DIAMOND_CHESTPLATE,
                Items.DIAMOND_LEGGINGS,
                Items.DIAMOND_BOOTS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.DIAMOND_TOOLS).apply {
            add(
                Items.DIAMOND_PICKAXE,
                Items.DIAMOND_SWORD,
                Items.DIAMOND_AXE,
                Items.DIAMOND_HOE,
                Items.DIAMOND_SHOVEL
            )
        }
        getOrCreateTagBuilder(VminusItemTags.NETHERITE_EQUIPMENT).apply {
            addOptionalTags(
                VminusItemTags.NETHERITE_TOOLS,
                VminusItemTags.NETHERITE_ARMORS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.NETHERITE_ARMORS).apply {
            add(
                Items.NETHERITE_HELMET,
                Items.NETHERITE_CHESTPLATE,
                Items.NETHERITE_LEGGINGS,
                Items.NETHERITE_BOOTS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.NETHERITE_TOOLS).apply {
            add(
                Items.NETHERITE_PICKAXE,
                Items.NETHERITE_SWORD,
                Items.NETHERITE_AXE,
                Items.NETHERITE_HOE,
                Items.NETHERITE_SHOVEL
            )
        }
        getOrCreateTagBuilder(VminusItemTags.GOLDEN_EQUIPMENT).apply {
            addOptionalTags(
                VminusItemTags.GOLDEN_TOOLS,
                VminusItemTags.GOLDEN_ARMORS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.GOLDEN_ARMORS).apply {
            add(
                Items.GOLDEN_HELMET,
                Items.GOLDEN_CHESTPLATE,
                Items.GOLDEN_LEGGINGS,
                Items.GOLDEN_BOOTS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.GOLDEN_TOOLS).apply {
            add(
                Items.GOLDEN_PICKAXE,
                Items.GOLDEN_SWORD,
                Items.GOLDEN_AXE,
                Items.GOLDEN_HOE,
                Items.GOLDEN_SHOVEL
            )
        }
        getOrCreateTagBuilder(VminusItemTags.IRON_EQUIPMENT).apply {
            addOptionalTags(
                VminusItemTags.IRON_TOOLS,
                VminusItemTags.IRON_ARMORS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.IRON_ARMORS).apply {
            add(
                Items.IRON_HELMET,
                Items.IRON_CHESTPLATE,
                Items.IRON_LEGGINGS,
                Items.IRON_BOOTS,
            )
        }
        getOrCreateTagBuilder(VminusItemTags.IRON_TOOLS).apply {
            add(
                Items.IRON_PICKAXE,
                Items.IRON_SWORD,
                Items.IRON_AXE,
                Items.IRON_HOE,
                Items.IRON_SHOVEL
            )
        }
        getOrCreateTagBuilder(VminusItemTags.STONE_TOOLS).apply {
            add(
                Items.STONE_PICKAXE,
                Items.STONE_SWORD,
                Items.STONE_AXE,
                Items.STONE_HOE,
                Items.STONE_SHOVEL
            )
        }
    }
}
