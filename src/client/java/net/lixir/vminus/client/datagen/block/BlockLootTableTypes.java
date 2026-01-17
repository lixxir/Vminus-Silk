package net.lixir.vminus.client.datagen.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lixir.vminus.client.definition.datagen.BlockDatagenDefinition;
import net.minecraft.block.Block;
import org.apache.logging.log4j.util.TriConsumer;

/**
 * Default VMinus-provided block loot table types.
 */
@AllArgsConstructor
@Getter
public enum BlockLootTableTypes implements BlockLootTableType {
    UNSET("unset", (block, definition, provider) -> {
    }),
    NONE("none", (block, definition, provider) -> {
    });
    /*
    SHEARS("shears", (data, generator) -> generator.shears(data.getBlock())),
    TALL_PLANT_SHEARS("tall_plant_shears", (data, generator) -> generator.doublePlantShears(data.getBlock())),
    SELF("self", (data, generator) -> generator.self(data.getBlock())),
    NYLIUM("self", (data, generator) -> generator.nylium(data.getBlock())),
    TALL_FLOWER("tall_flower", (data, generator) -> generator.tallFlower(data.getBlock())),
    FLOWERBED("flowerbed", (data, generator) -> generator.flowerbed((FlowerbedBlock) data.getBlock())),
    FLOWERBED_SHEARS("flowerbed_shears", (data, generator) -> generator.flowerbedShears((FlowerbedBlock) data.getBlock()));

     */

    private final String name;
    private final TriConsumer<Block, BlockDatagenDefinition, VBlockLootTableGenerator> consumer;

    @Override
    public void apply(Block block, BlockDatagenDefinition definition, VBlockLootTableGenerator generator) {
        consumer.accept(block, definition, generator);
    }
}
