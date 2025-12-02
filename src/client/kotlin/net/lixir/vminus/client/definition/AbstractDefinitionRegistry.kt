package net.lixir.vminus.client.definition

import net.lixir.vminus.Vminus
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.Item

abstract class AbstractDefinitionRegistry<P : AbstractDefinitionGroupProvider<*>> protected constructor(
    protected val modId: String,
    definitionGroupProvider: P
) {
    val providers: MutableList<RegistryDefinitionProvider<*>> = mutableListOf()

    init {
        definitionGroupProvider.generate()
    }

    fun addProvider(provider: RegistryDefinitionProvider<*>) {
        providers.add(provider)
        provider.run()
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        private fun <T : Any, D : AbstractDefinition<D, T>, G : AbstractDefinitionGroup<T>, P : AbstractDefinitionGroupProvider<G>> getDefaultDefinition(
            targetObject: T,
            definitionCategory: DefinitionCategory
        ): D? {
            val targetClass = targetObject::class.java
            val compatibleGroups = mutableListOf<AbstractDefinitionGroup<*>>()
            var bestGroup: AbstractDefinitionGroup<*>? = null
            var bestDistance = Int.MAX_VALUE
            val groupDistances = mutableMapOf<AbstractDefinitionGroup<*>, Int>()

            val providers = AbstractDefinitionGroupProvider.getAllProvidersOfType<P>(definitionCategory)
            for (provider in providers) {
                for ((groupClass, value) in provider.assignedClasses) {
                    if (groupClass.isAssignableFrom(targetClass)) {
                        val group = value as AbstractDefinitionGroup<*>
                        compatibleGroups.add(group)

                        val distance = getClassDistance(targetClass, groupClass)
                        groupDistances[group] = distance

                        if (distance < bestDistance) {
                            bestDistance = distance
                            bestGroup = group
                        }
                    }
                }

                for ((key, value) in provider.assignedSuppliers) {
                    val supplied = key.get()
                    if (targetObject == supplied) {
                        bestGroup = value
                        break
                    }
                }
            }

            if (bestGroup == null) return null

            val bestEntry = bestGroup.definition as D
            val sortedGroups = compatibleGroups
                .filter { it != bestGroup }
                .sortedBy { groupDistances.getOrDefault(it, Int.MAX_VALUE) }

            for (group in sortedGroups) {
                val otherEntry = group.definition as? D ?: continue
                bestEntry.merge(otherEntry)
            }

            Vminus.devLogger("Best Definition for $targetObject of $definitionCategory is $bestEntry")
            return bestEntry
        }

        private fun getClassDistance(child: Class<*>?, parent: Class<*>): Int {
            var child = child
            var distance = 0
            while (child != null && child != parent) {
                child = child.superclass
                distance++
            }
            return if (child == null) Int.MAX_VALUE else distance
        }

        fun getDefaultEntityDefinition(
            entityType: EntityType<*>,
            definitionCategory: DefinitionCategory
        ): AbstractDefinition<*, EntityType<*>>? =
            getDefaultDefinition(entityType, definitionCategory)

        fun getDefaultItemDefinition(
            item: Item,
            definitionCategory: DefinitionCategory
        ): AbstractDefinition<*, Item>? =
            getDefaultDefinition(item, definitionCategory)

        fun getDefaultBlockDefinition(
            block: Block,
            definitionCategory: DefinitionCategory
        ): AbstractDefinition<*, Block>? =
            getDefaultDefinition(block, definitionCategory)
    }
}
