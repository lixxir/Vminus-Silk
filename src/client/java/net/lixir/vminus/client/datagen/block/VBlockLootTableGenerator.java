package net.lixir.vminus.client.datagen.block;

import lombok.Getter;
import net.lixir.vminus.client.definition.datagen.BlockDataGenDefinitionEntry;
import net.lixir.vminus.client.definition.datagen.BlockDataGenDefinition;
import net.lixir.vminus.registry.VRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

/**
 * Base class for generating block loot tables in VMinus.
 */
@Getter
public abstract class VBlockLootTableGenerator extends BlockLootTableGenerator {
    protected final String modId;

    protected VBlockLootTableGenerator(String modId, Set<Item> explosionImmuneItems, FeatureSet requiredFeatures, RegistryWrapper.WrapperLookup registryLookup) {
        super(explosionImmuneItems, requiredFeatures, registryLookup);
        this.modId = modId;
    }

    @Override
    public void generate() {
        var blocks = VRegistry.Companion.fromId(modId).getBlocks();
        for (Block block : blocks) {
            if (block.getLootTableKey().equals(LootTables.EMPTY))
                continue;
            BlockDataGenDefinition blockDefinition = BlockDataGenDefinition.Companion.of(block);
            BlockLootTableType tableType = blockDefinition.getLootTableType();
            if (tableType.isEmpty())
                continue;
            tableType.apply(BlockDataGenDefinitionEntry.Companion.of(block), this);
        }
    }

    public void tallFlower(Block block) {
        EnumProperty<DoubleBlockHalf> half = TallPlantBlock.HALF;

        this.addDrop(block, LootTable.builder().pool(
                this.applyExplosionDecay(block,
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .conditionally(MatchToolLootCondition.builder(
                                        ItemPredicate.Builder.create()
                                                .items(Items.SHEARS)
                                ))
                                .with(ItemEntry.builder(block))
                )
        ));
    }

    public void doublePlantShears(Block block) {
        /*
        this.addDrop(block, createDoublePlantShearsDrop(block));

         */
    }

    public void shears(Block block) {
        /*
        this.addDrop(block, createShearsOnlyDrop(block));

         */
    }

    public void nylium(Block block) {
        /*
        this.addDrop(block, LootTable.builder().pool(
                this.applyExplosionDecay(block,
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .conditionally(MatchToolLootCondition.builder(
                                        ItemPredicate.Builder.create()
                                                .enchantment(new EnchantmentPredicate(
                                                        Enchantments.SILK_TOUCH,
                                                        NumberRange.IntRange.atLeast(1)
                                                ))
                                ))
                                .with(ItemEntry.builder(block))
                                .with(ItemEntry.builder(Items.NETHERRACK))
                )
        ));

         */
    }

    public void self(Block block) {
        /*
        dropSelf(block);

         */
    }

    public void flowerbedShears(FlowerbedBlock block) {
        IntProperty amount = FlowerbedBlock.FLOWER_AMOUNT;

        this.addDrop(block, LootTable.builder().pool(
                this.applyExplosionDecay(block,
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .conditionally(MatchToolLootCondition.builder(
                                        ItemPredicate.Builder.create().items(Items.SHEARS)
                                ))
                                .with(ItemEntry.builder(block)
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)))
                                )
                )
        ));
    }

    public void flowerbed(FlowerbedBlock block) {
        IntProperty amount = FlowerbedBlock.FLOWER_AMOUNT;

        this.addDrop(block, LootTable.builder().pool(
                this.applyExplosionDecay(block,
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(block)
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)))
                                )
                )
        ));
    }

    public <T> Optional<T> as(@NotNull Class<T> clazz) {
        return clazz.isInstance(this) ? Optional.of(clazz.cast(this)) : Optional.empty();
    }
}
