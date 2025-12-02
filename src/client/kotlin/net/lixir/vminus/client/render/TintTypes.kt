package net.lixir.vminus.client.render

import lombok.AllArgsConstructor
import lombok.Getter
import net.lixir.vminus.client.render.BlockTintFunction
import net.minecraft.client.color.world.BiomeColors
import net.minecraft.world.biome.FoliageColors
import net.minecraft.world.biome.GrassColors
import org.jetbrains.annotations.Contract


enum class TintTypes(
    override val id: String,
    override val itemTintFunction: ItemTintFunction? = null,
    override val blockTintFunction: BlockTintFunction? = null
) : TintType {
    UNSET("unset"),
    NONE("none"),
    FOLIAGE(
        "foliage",
        blockTintFunction = BlockTintFunction { _, world, pos, _ ->
            if (world != null && pos != null) BiomeColors.getFoliageColor(world, pos)
            else FoliageColors.getDefaultColor()
        }
    ),
    GRASS(
        "grass",
        itemTintFunction = ItemTintFunction { _, tintIndex ->
            if (tintIndex == 0) GrassColors.getDefaultColor() else -1
        },
        blockTintFunction = BlockTintFunction { _, world, pos, _ ->
            if (world != null && pos != null) BiomeColors.getGrassColor(world, pos)
            else GrassColors.getDefaultColor()
        }
    );

    override fun asString(): String = id

    override fun toString(): String = "TintType($id)"
}
