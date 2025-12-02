package net.lixir.vminus.block

import net.minecraft.block.Block
import java.util.function.Supplier


open class StrippableLogBlock(settings: Settings, private val stripped: Supplier<Block>) :
    LogBlock(settings) {
    fun getStripped(): Block {
        return stripped.get()
    }
}