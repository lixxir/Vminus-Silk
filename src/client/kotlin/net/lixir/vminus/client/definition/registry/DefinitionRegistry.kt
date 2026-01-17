package net.lixir.vminus.client.definition.registry

import net.lixir.vminus.Vminus
import net.lixir.vminus.client.definition.Definition
import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionProvider
import net.lixir.vminus.client.definition.SimpleProvider
import net.lixir.vminus.registry.VRegistry
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.Item

abstract class DefinitionRegistry<P : DefinitionProvider<*, *>> protected constructor(
    val modId: String
) {
    protected val definitionProviders: MutableList<P> = mutableListOf()
    protected val simpleProviders: MutableList<SimpleProvider> = mutableListOf()

    fun addDefinitionProvider(provider: P) {
        definitionProviders.add(provider)
        provider.generate()
    }

    fun addSimpleProvider(provider: SimpleProvider) {
        simpleProviders.add(provider)
        provider.run()
    }

    abstract fun generateDefinitions(vRegistry: VRegistry)

    companion object {

        private fun <T : Any, D : Definition<T>, P : DefinitionProvider<D, T>> getDefaultDefinition(
            targetObject: T,
            definitionCategory: DefinitionCategory
        ): D? {
            val targetClass = targetObject::class.java
            val definitions = mutableListOf<Definition<*>>()
            var bestDefinition: D? = null
            var bestClassDistance = Int.MAX_VALUE
            val classDistances = mutableMapOf<Definition<*>, Int>()

            val providers = DefinitionProvider.getProvidersInCategory<P>(definitionCategory)
            for (provider in providers) {
                for ((groupClass, value) in provider.assignedClasses) {
                    if (groupClass.isAssignableFrom(targetClass)) {
                        definitions.add(value)

                        val distance = getClassDistance(targetClass, groupClass)
                        classDistances[value] = distance

                        if (distance < bestClassDistance) {
                            bestClassDistance = distance
                            bestDefinition = value
                        }
                    }
                }

                for ((key, value) in provider.assignedSuppliers) {
                    val supplied = key.get()
                    if (targetObject == supplied) {
                        bestDefinition = value
                        break
                    }
                }
            }
            Vminus.devLogger("Trying")
            if (bestDefinition == null) return null
            Vminus.devLogger("Passed")
            val sortedDefinitions: List<D> = definitions
                .filter { it != bestDefinition }
                .sortedBy { classDistances.getOrDefault(it, Int.MAX_VALUE) } as List<D>

            for (definition in sortedDefinitions) {
                bestDefinition.merge(definition)
            }

            Vminus.devLogger("Best Definition for $targetObject of $definitionCategory is $bestDefinition")
            return bestDefinition
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
        ): Definition<EntityType<*>>? =
            getDefaultDefinition(entityType, definitionCategory)

        fun getDefaultItemDefinition(
            item: Item,
            definitionCategory: DefinitionCategory
        ): Definition<Item>? =
            getDefaultDefinition(item, definitionCategory)

        fun getDefaultBlockDefinition(
            block: Block,
            definitionCategory: DefinitionCategory
        ): Definition<Block>? =
            getDefaultDefinition(block, definitionCategory)
    }
}
