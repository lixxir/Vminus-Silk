package net.lixir.vminus.registry

import com.mojang.serialization.MapCodec
import net.lixir.vminus.Vminus
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory
import net.minecraft.component.ComponentType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.fluid.Fluid
import net.minecraft.item.BlockItem
import net.minecraft.item.DyeItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.particle.SimpleParticleType
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.foliage.FoliagePlacerType
import net.minecraft.world.gen.treedecorator.TreeDecorator
import net.minecraft.world.gen.treedecorator.TreeDecoratorType
import net.minecraft.world.gen.trunk.TrunkPlacer
import net.minecraft.world.gen.trunk.TrunkPlacerType
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.UnaryOperator
import net.minecraft.util.Identifier.of as ofId

open class VRegistry(val modId: String) {
    init {
        REGISTRIES.putIfAbsent(modId, this)
    }

    val blocks: DefaultedList<Block> = DefaultedList.of()
    val entityTypes: DefaultedList<EntityType<*>> = DefaultedList.of()
    val soundEvents: DefaultedList<SoundEvent> = DefaultedList.of()
    val items: DefaultedList<Item> = DefaultedList.of()
    val fluids: DefaultedList<Fluid> = DefaultedList.of()

    fun <T : Fluid?> fluid(id: String, fluid: T): T {
        val location = ofId(modId, id)
        val registered = Registry.register(Registries.FLUID, location, fluid)
        fluids.add(registered)
        return registered
    }

    fun blockItem(id: String? = null, block: Block, blockItem: BlockItem = BlockItem(block, Item.Settings())): Item {
        var id = id
        if (id == null) {
            id = Registries.BLOCK.getId(block).path
        }
        return item(id!!, blockItem)
    }

    fun blockItem(blockItem: BlockItem): Item {
        val block = blockItem.block
        return this.blockItem(block = block, blockItem = blockItem)
    }

    fun block(id: String, block: Block = Block(AbstractBlock.Settings.create())): Block {
        val identifier = ofId(modId, id)
        val registered = Registry.register(Registries.BLOCK, identifier, block)
        blocks.add(registered)
        return registered
    }

    fun item(id: String, item: Item = Item(Item.Settings())): Item {
        val registered = Registry.register(Registries.ITEM, ofId(modId, id), item)
        items.add(registered)
        if (registered is BlockItem) registered.appendBlocks(Item.BLOCK_ITEMS, registered)
        return registered
    }

    fun <T> dataComponentType(
        id: String,
        builderOperator: UnaryOperator<ComponentType.Builder<T>>,
    ): ComponentType<T> {
        return Registry.register(
            Registries.DATA_COMPONENT_TYPE, Identifier.tryParse(modId, id), builderOperator.apply(
                ComponentType.builder()
            ).build()
        )
    }

    fun statusEffect(id: String, statusEffect: StatusEffect): RegistryEntry<StatusEffect> =
        Registry.registerReference(Registries.STATUS_EFFECT, ofId(modId, id), statusEffect)

    fun <T : Entity> entityType(id: String, entityTypeBuilder: EntityType.Builder<T>): EntityType<T> {
        val entityType =
            Registry.register(Registries.ENTITY_TYPE, ofId(modId, id), entityTypeBuilder.build(id))
        entityTypes.add(entityType)
        return entityType
    }

    fun itemGroup(id: String, creativeModeTab: ItemGroup): ItemGroup =
        Registry.register(Registries.ITEM_GROUP, ofId(modId, id), creativeModeTab)

    fun feature(id: String, feature: Feature<*>): Feature<*> =
        Registry.register(Registries.FEATURE, ofId(modId, id), feature)

    fun <T : FoliagePlacer> foliagePlacerType(id: String, codec: MapCodec<T>): FoliagePlacerType<T> =
        Registry.register(Registries.FOLIAGE_PLACER_TYPE, ofId(modId, id), FoliagePlacerType(codec))

    fun <T : FoliagePlacer> foliagePlacerType(
        id: String,
        foliagePlacerType: FoliagePlacerType<T>,
    ): FoliagePlacerType<T> =
        Registry.register(Registries.FOLIAGE_PLACER_TYPE, ofId(modId, id), foliagePlacerType)

    fun <T : TrunkPlacer> trunkPlacerType(id: String, codec: MapCodec<T>?): TrunkPlacerType<T> =
        Registry.register(Registries.TRUNK_PLACER_TYPE, ofId(modId, id), TrunkPlacerType(codec))

    fun <T : TrunkPlacer?> trunkPlacer(id: String, trunkPlacerType: TrunkPlacerType<T>): TrunkPlacerType<T> =
        Registry.register(Registries.TRUNK_PLACER_TYPE, ofId(modId, id), trunkPlacerType)

    fun attribute(id: String, attribute: EntityAttribute): RegistryEntry<EntityAttribute> =
        Registry.registerReference(Registries.ATTRIBUTE, ofId(modId, id), attribute)

    fun attribute(
        id: String,
        defaultValue: Double,
        minimumValue: Double,
        maximumValue: Double,
    ): RegistryEntry<EntityAttribute> {
        return Registry.registerReference(
            Registries.ATTRIBUTE, ofId(modId, id), ClampedEntityAttribute(
                "attribute.$modId.key.$id", defaultValue, minimumValue, maximumValue
            ).setTracked(true)
        )
    }

    fun dyeItem(id: String, dyeColor: DyeColor): Item
        = item(id, DyeItem(dyeColor, Item.Settings()))

    fun <S : RecipeSerializer<T>?, T : Recipe<*>?> recipeSerializer(id: String, serializer: S): S {
        return Registry.register(Registries.RECIPE_SERIALIZER, ofId(modId, id), serializer)
    }

    fun <T : Recipe<*>> recipeType(id: String): RecipeType<T> {
        return Registry.register(Registries.RECIPE_TYPE, ofId(modId, id), object : RecipeType<T> {
            override fun toString(): String {
                return id
            }
        })
    }

    fun <T : BlockEntity> blockEntityType(
        id: String,
        factory: BlockEntityFactory<T>,
        clazz: Class<out Block>,
    ): BlockEntityType<T> {
        val blocks = getBlocksOfClass(clazz).toTypedArray()
        return blockEntityType(id, factory, *blocks)
    }

    fun <T : BlockEntity> blockEntityType(
        id: String,
        factory: BlockEntityFactory<T>,
        vararg blocks: Block,
    ): BlockEntityType<T> {
        if (blocks.isEmpty())
            Vminus.LOGGER.warn("Block entity type {} requires at least one valid block to be defined!", id)

        return Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            ofId(modId, id),
            BlockEntityType.Builder.create(factory, *blocks).build(null)
        )
    }

    fun soundEvent(id: String): SoundEvent {
        val id = ofId(modId, id)
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    }

    fun simpleParticleType(id: String, alwaysShow: Boolean = false): SimpleParticleType
        = particleType(id, SimpleParticleType(alwaysShow)) as SimpleParticleType

    fun soundEventReference(id: String): RegistryEntry.Reference<SoundEvent> {
        val soundEvent = Registry.register(
            Registries.SOUND_EVENT,
            ofId(modId, id),
            SoundEvent.of(ofId(modId, id))
        )
        return Registries.SOUND_EVENT.getEntry(Registries.SOUND_EVENT.getRawId(soundEvent)).orElseThrow()
    }

    fun <T : ParticleEffect> particleType(id: String, particleType: ParticleType<T>): ParticleType<T>
       = Registry.register(Registries.PARTICLE_TYPE, ofId(modId, id), particleType)

    fun <T : TreeDecorator> treeDecoratorType(
        id: String,
        treeDecoratorType: TreeDecoratorType<T>,
    ): TreeDecoratorType<T> {
        return Registry.register(Registries.TREE_DECORATOR_TYPE, ofId(modId, id), treeDecoratorType)
    }

    fun getBlocksOfClass(vararg clazz: Class<out Block>): List<Block> {
        return blocks.stream()
            .filter { block: Block ->
                for (c in clazz) {
                    if (c.isInstance(block)) return@filter true
                }
                false
            }
            .toList()
    }

    companion object {
        private val REGISTRIES = ConcurrentHashMap<String, VRegistry?>()
        val registries: List<VRegistry?>
            get() = REGISTRIES.values.stream().toList()

        fun fromId(id: String): VRegistry = REGISTRIES[id]!!
    }
}
