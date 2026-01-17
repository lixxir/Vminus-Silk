package net.lixir.vminus.client.datagen.block;


import net.lixir.vminus.client.definition.datagen.BlockDatagenDefinition;
import net.minecraft.block.Block;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;

/**
 * Defines a type of block loot table behavior.
 * <p>
 * Implementations (usually enums like {@link BlockLootTableTypes})
 * determine how loot is generated for a block during data generation.
 * Each type wraps a {@link BiConsumer} that applies loot rules for a block
 * and its {@link BlockDatagenDefinition}.
 */
public interface BlockLootTableType {
    void apply(Block block, BlockDatagenDefinition definition, VBlockLootTableGenerator generator);

    TriConsumer<Block, BlockDatagenDefinition, VBlockLootTableGenerator> getConsumer();

    String getName();

    default boolean isEmpty() {
        return this.getName().equals(BlockLootTableTypes.NONE.getName()) || isUnset();
    }

    default boolean isUnset() {
        return this == BlockLootTableTypes.UNSET;
    }
}

