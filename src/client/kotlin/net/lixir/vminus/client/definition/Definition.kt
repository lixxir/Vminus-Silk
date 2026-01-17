package net.lixir.vminus.client.definition

import net.minecraft.util.Identifier

abstract class Definition<T> protected constructor() {
    var inherits = false

    protected abstract val category: DefinitionCategory
    abstract fun setDefault(of: T): Definition<T>

    abstract val empty: Boolean

    open fun copy(): Definition<T> = this

    open fun merge(other: Definition<T>?): Definition<T> = this

    companion object {
        const val STRING_UNSET: String = "unset"
        val IDENTIFIER_UNSET: Identifier = Identifier.ofVanilla("unset")
    }
}
