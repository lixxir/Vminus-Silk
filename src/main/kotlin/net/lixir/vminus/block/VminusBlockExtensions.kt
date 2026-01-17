package net.lixir.vminus.block

import net.lixir.vminus.util.Identifiable
import net.minecraft.block.Block
import net.minecraft.util.Identifier

object VminusBlockExtensions {
    fun Block.getIdentifier(): Identifier = (this as Identifiable).identifier
}