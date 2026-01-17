package net.lixir.vminus.client.definition

import net.lixir.vminus.Vminus
import java.util.*
import java.util.function.Supplier
import kotlin.collections.ArrayList

abstract class DefinitionProvider<D : Definition<T>, T> protected constructor() {
    val assignedClasses: MutableMap<Class<out T>, D> = HashMap()
    val assignedSuppliers: MutableMap<Supplier<out T>, D> = HashMap()

    protected abstract val category: DefinitionCategory

    abstract fun run()

    fun generate() {
        PROVIDERS.computeIfAbsent(category) { ArrayList() }.add(
            this
        )
        run()
        Vminus.devLogger("Generated definitions for category $category of $assignedClasses")
    }

    fun assign(clazz: Class<out T>, vararg definitions: D) {
        for (definition in definitions) {
            val storedDefinition = assignedClasses[clazz]
            if (storedDefinition == null)
                assignedClasses[clazz] = definition
            else
                assignedClasses[clazz]!!.merge(definition)
        }

    }

    fun assign(supplier: Supplier<out T>, vararg definitions: D) {
        for (definition in definitions) {
            val storedDefinition = assignedSuppliers[supplier]
            if (storedDefinition == null)
                assignedSuppliers[supplier] = definition
            else
                assignedSuppliers[supplier]!!.merge(definition)
        }
    }

    companion object {
        private val PROVIDERS: MutableMap<DefinitionCategory, MutableList<DefinitionProvider<*, *>>> = HashMap()

        val allProviders: Collection<List<DefinitionProvider<*, *>>>
            get() = PROVIDERS.values

        @Suppress("UNCHECKED_CAST")
        fun <P : DefinitionProvider<*, *>> getProvidersInCategory(category: DefinitionCategory): List<P> {
            Vminus.devLogger("Looking for providers of category: $category")
            val results: MutableList<P> = ArrayList()

            for ((key, value) in PROVIDERS) {
                if (category == key) {
                    for (provider in value) {
                        results.add(provider as P)
                    }
                }
            }
            Vminus.devLogger("Found: $results")
            return results
        }
    }
}
