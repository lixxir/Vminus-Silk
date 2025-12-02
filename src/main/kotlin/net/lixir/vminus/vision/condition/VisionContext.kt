package net.lixir.vminus.vision.condition

import net.lixir.vminus.duck.VisionDuck
import net.minecraft.block.AbstractBlock.AbstractBlockState
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

class VisionContext {

    var itemGroup: ItemGroup? = null
    var item: Item? = null
        private set
    var itemStack: ItemStack? = null
        private set
    var entity: Entity? = null
        private set
    var block: Block? = null
        private set
    var abstractBlockState: AbstractBlockState? = null
        private set
    var blockState: BlockState? = null
        private set
    var statusEffect: StatusEffect? = null
    var entityType: EntityType<*>? = null
        private set

    private constructor(builder: Builder) {
        this.item = builder.item
        this.entity = builder.entity
        this.itemStack = builder.itemStack
        this.block = builder.block
        this.abstractBlockState = builder.abstractBlockState
        this.blockState = builder.blockState
    }

    constructor(itemEntity: ItemEntity?) {
        if (itemEntity == null) return
        val stack = itemEntity.stack
        this.itemStack = stack
        this.item = stack.item
    }

    constructor(itemStack: ItemStack?) {
        if (itemStack == null) return
        this.item = itemStack.item
        this.itemStack = itemStack
    }

    constructor(item: Item?) {
        this.item = item
    }

    constructor(itemGroup: ItemGroup?) {
        this.itemGroup = itemGroup
    }
    constructor(statusEffect: StatusEffect?) {
        this.statusEffect = statusEffect
    }

    constructor(entityType: EntityType<*>?) {
        this.entityType = entityType
    }

    constructor(entity: Entity) {
        this.entity = entity
        this.entityType = entity.type
    }

    constructor(block: Block?) {
        this.block = block
    }

    constructor(state: AbstractBlockState?) {
        this.abstractBlockState = state
    }


    fun hasItem(): Boolean {
        return item != null
    }

    fun hasItemStack(): Boolean {
        return itemStack != null
    }

    fun hasEntity(): Boolean {
        return entity != null
    }


    class Builder {
        internal var item: Item? = null
        var block: Block? = null
        var entity: Entity? = null
        var itemStack: ItemStack? = null
        var abstractBlockState: AbstractBlockState? = null
        var blockState: BlockState? = null

        fun pass(item: Item?): Builder {
            this.item = item
            return this
        }

        fun pass(itemStack: ItemStack): Builder {
            this.itemStack = itemStack
            if (this.item == null) this.item = itemStack.item
            return this
        }

        fun pass(entity: Entity?): Builder {
            this.entity = entity
            return this
        }

        fun pass(block: Block): Builder {
            this.block = block
            if (this.item == null) this.item = block.asItem()
            return this
        }

        fun pass(blockState: BlockState): Builder {
            this.blockState = blockState
            if (this.block == null) this.block = blockState.block
            if (this.item == null) this.item = block!!.asItem()
            return this
        }

        fun pass(state: AbstractBlockState): Builder {
            this.abstractBlockState = state
            if (this.block == null) this.block = state.block
            if (this.item == null) this.item = block!!.asItem()
            return this
        }

        fun build(): VisionContext {
            return VisionContext(this)
        }
    }

    companion object {
        fun fromDuck(duck: VisionDuck): VisionContext {
            return when (duck) {
                is Block -> VisionContext(duck)
                is BlockState -> VisionContext(duck.block)
                is Entity -> VisionContext(duck)
                is EntityType<*> -> VisionContext(duck)
                is Item -> VisionContext(duck)
                is ItemStack -> VisionContext(duck)
                is StatusEffect -> VisionContext(duck)
                is ItemGroup -> VisionContext(duck)
                else -> throw IllegalArgumentException("Cannot create VisionContext from $duck")
            }
        }
    }
}
