package net.lixir.vminus.block

import net.minecraft.block.BlockState
import net.minecraft.state.property.Property


object BlockStateUtils {

    @Suppress("UNCHECKED_CAST")
    fun <T : Comparable<T>?> reverseCycle(state: BlockState, property: Property<T>): BlockState {
        val values = property.values
        val list = if (values is List<*>) values as List<T> else ArrayList(values)
        val current = state.get(property)
        val index = list.indexOf(current)
        val prevIndex = (index - 1 + list.size) % list.size

        return state.with(property, list[prevIndex])
    }

    fun copyProperties(from: BlockState, to: BlockState): BlockState {
        var toState = to
        for (property in from.properties) {
            if (toState.properties.contains(property)) {
                toState = copyProperty(from, toState, property)
            }
        }
        return toState
    }

    private fun copyProperty(
        from: BlockState,
        to: BlockState,
        property: Property<out Comparable<*>>
    ): BlockState {
        @Suppress("UNCHECKED_CAST")
        return to.with(property as Property<Comparable<Any>>, from.get(property) as Comparable<Any>)
    }
}
