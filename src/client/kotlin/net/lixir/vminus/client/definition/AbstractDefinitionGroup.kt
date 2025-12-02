package net.lixir.vminus.client.definition

abstract class AbstractDefinitionGroup<T> protected constructor(definition: AbstractDefinition<*, T>) {
    var definition: AbstractDefinition<*, T>
         protected set

    init {
        require(!definition.isDefaulted) { "Definition Groups cannot contain defaulted Definitions!" }
        this.definition = definition
    }
}
