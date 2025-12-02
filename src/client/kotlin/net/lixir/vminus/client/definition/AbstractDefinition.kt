package net.lixir.vminus.client.definition

import net.minecraft.util.Identifier

abstract class AbstractDefinition<D : AbstractDefinition<D, T>, T> protected constructor() {
    var isDefaulted: Boolean = false

    protected abstract val type: DefinitionCategory?
    abstract fun setDefault(of: T): D

    fun setDefaulted() {
        this.isDefaulted = true
    }

    /**
     * Pass a static instance of an empty definition as an argument to .equals() for this implementation.
     *
     * @return Returns true if the definition is effectively empty.
     */
    abstract val isEmpty: Boolean

    @Suppress("UNCHECKED_CAST")
    open fun merge(other: D?): D = this as D

    companion object {
        val UNSET_RESOURCE_LOCATION: Identifier = Identifier.ofVanilla("unset")
    }
}
