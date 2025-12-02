package net.lixir.vminus.client.datagen.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lixir.vminus.client.definition.datagen.BlockDataGenDefinitionEntry;
import net.minecraft.block.FlowerbedBlock;

import java.util.function.BiConsumer;

/**
 * Default VMinus-provided block loot table types.
 */
@AllArgsConstructor
@Getter
public enum BlockLootTableTypes implements BlockLootTableType {
    UNSET("unset", (data, provider) -> {
    }),
    NONE("none", (data, provider) -> {
    }),
    SHEARS("shears", (data, provider) -> provider.shears(data.getBlock())),
    TALL_PLANT_SHEARS("tall_plant_shears", (data, provider) -> provider.doublePlantShears(data.getBlock())),
    SELF("self", (data, provider) -> provider.self(data.getBlock())),
    NYLIUM("self", (data, provider) -> provider.nylium(data.getBlock())),
    TALL_FLOWER("tall_flower", (data, provider) -> provider.tallFlower(data.getBlock())),
    FLOWERBED("flowerbed", (data, provider) -> provider.flowerbed((FlowerbedBlock) data.getBlock())),
    FLOWERBED_SHEARS("flowerbed_shears", (data, provider) -> provider.flowerbedShears((FlowerbedBlock) data.getBlock()));

    private final String name;
    private final BiConsumer<BlockDataGenDefinitionEntry, VBlockLootTableGenerator> consumer;

    @Override
    public void apply(BlockDataGenDefinitionEntry blockDatagenDefinitionEntry, VBlockLootTableGenerator provider) {
        consumer.accept(blockDatagenDefinitionEntry, provider);
    }
}
