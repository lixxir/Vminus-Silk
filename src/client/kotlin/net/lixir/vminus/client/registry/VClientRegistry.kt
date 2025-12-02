package net.lixir.vminus.client.registry

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.lixir.vminus.client.render.TintType
import net.lixir.vminus.registry.VRegistry
import net.lixir.vminus.registry.VRegistry.Companion
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.world.ClientWorld
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class VClientRegistry(val modId: String) {
    init {
        REGISTRIES.putIfAbsent(modId, this)
    }

    val tintedBlocks: HashMap<Block, TintType> = HashMap()
    val tintedItems: HashMap<Item, TintType> = HashMap()

    fun isItemTinted(item: Item): Boolean = tintedItems.containsKey(item)

    fun isBlockTinted(block: Block): Boolean = tintedBlocks.containsKey(block)

    inline fun <reified T : ParticleEffect> particleFactory(
        particleType: ParticleType<T>,
        crossinline factory: (spriteProvider: FabricSpriteProvider) -> ParticleFactory<T>
    ) {
        ParticleFactoryRegistry.getInstance().register(particleType) { spriteProvider ->
            factory(spriteProvider)
        }
    }


    fun tintBlocks(tintType: TintType, vararg blocks: Block) {
        val function = tintType.blockTintFunction ?: return
        ColorProviderRegistry.BLOCK.register(BlockColorProvider { state: BlockState?, world: BlockRenderView?, pos: BlockPos?, index: Int ->
            function.apply(
                state,
                world,
                pos,
                index
            )
        }, *blocks)
        Arrays.stream(blocks).forEach { block: Block ->
            tintedBlocks[block] = tintType
        }
    }

    fun tintItems(tintType: TintType, vararg items: Item) {
        val function = tintType.itemTintFunction ?: return
        ColorProviderRegistry.ITEM.register(ItemColorProvider { stack: ItemStack?, tintIndex: Int ->
            function.apply(
                stack,
                tintIndex
            )
        }, *items)
        Arrays.stream(items).forEach { item: Item ->
            tintedItems[item] = tintType
        }
    }

    fun itemModel(parent: String, vararg requiredTextureKeys: TextureKey): Model =
        itemModel(id = Identifier.of(modId, "item/$parent"), requiredTextureKeys = requiredTextureKeys)

    fun itemModel(id: Identifier, vararg requiredTextureKeys: TextureKey): Model =
        Model(
            Optional.of(
                id
            ), Optional.empty(), *requiredTextureKeys
        )

    fun blockModel(parent: String, variant: Optional<String> = Optional.empty(), vararg requiredTextureKeys: TextureKey): Model =
        blockModel(
            id = Identifier.of(modId, "block/$parent"),
            variant = variant,
            requiredTextureKeys = requiredTextureKeys
        )

    fun blockModel(id: Identifier, variant: Optional<String> = Optional.empty(), vararg requiredTextureKeys: TextureKey): Model =
        Model(
            Optional.of(
                id
            ), variant, *requiredTextureKeys
        )

    fun model(vararg requiredTextureKeys: TextureKey): Model =
        Model(Optional.empty(), Optional.empty(), *requiredTextureKeys)

    companion object {
        private val REGISTRIES = ConcurrentHashMap<String, VClientRegistry?>()

        fun fromId(id: String): VClientRegistry = REGISTRIES[id]!!
    }
}
