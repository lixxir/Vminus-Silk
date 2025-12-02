package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.AbstractDefinitionGroup

open class DataGenDefinitionGroup<T> protected constructor(definition: DataGenDefinition<*, T>) :
    AbstractDefinitionGroup<T>(definition) {
    fun <D : DataGenDefinition<D, T>> replace(definition: DataGenDefinition<D, T>): DataGenDefinitionGroup<T> {
        this.definition = of(definition).definition
        return this
    }

    companion object {
        fun <D : DataGenDefinition<D, T>, T> of(definition: DataGenDefinition<D, T>): DataGenDefinitionGroup<T> {
            return DataGenDefinitionGroup(definition)
        }
    }
}
