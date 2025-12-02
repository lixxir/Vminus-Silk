package net.lixir.vminus.client.definition

import net.lixir.vminus.Vminus
import java.util.*
import java.util.function.Supplier

abstract class AbstractDefinitionGroupProvider<G : AbstractDefinitionGroup<*>> protected constructor() {
    val assignedClasses: MutableMap<Class<*>, G> = HashMap()
    val assignedSuppliers: MutableMap<Supplier<Any>, G> = HashMap()

    protected abstract val type: DefinitionCategory

    abstract fun run()

    fun generate() {
        PROVIDERS.computeIfAbsent(type) { ArrayList() }.add(
            this
        )
        run()
        Vminus.devLogger("Generated definitions of type: $type")
    }

    fun assignClasses(group: G, vararg classes: Class<*>) {
        for (clazz in classes) assignedClasses[clazz] = group
    }

    fun assignSuppliers(group: G, vararg suppliers: Supplier<Any>) {
        for (supplier in suppliers) assignedSuppliers[supplier] = group
    }

    companion object {
        private val PROVIDERS: MutableMap<DefinitionCategory, MutableList<AbstractDefinitionGroupProvider<*>?>> = HashMap()

        val allProviders: Collection<List<AbstractDefinitionGroupProvider<*>?>>
            get() = PROVIDERS.values

        @Suppress("UNCHECKED_CAST")
        fun <P : AbstractDefinitionGroupProvider<*>> getAllProvidersOfType(type: DefinitionCategory): List<P> {
            val results: MutableList<P> = ArrayList()

            for ((key, value) in PROVIDERS) {
                if (type === key) {
                    for (provider in value) {
                        results.add(provider as P)
                    }
                }
            }

            return results
        }
    }
}
