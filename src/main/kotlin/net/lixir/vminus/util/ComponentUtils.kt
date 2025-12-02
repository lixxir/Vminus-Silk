package net.lixir.vminus.util

import net.minecraft.component.Component
import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentType

object ComponentUtils {

    @Suppress("UNCHECKED_CAST")
    fun mergeComponentChanges(original: ComponentMap?, changesList: List<ComponentChanges>): ComponentMap {
        val builder = ComponentMap.builder()
        if (original != null) {
            builder.addAll(original)
        }

        for (changes in changesList) {
            for ((type, valueOpt) in changes.entrySet()) {
                if (valueOpt.isPresent) {
                    val value = valueOpt.get()
                    builder.add(type as ComponentType<Any>, value as Any)
                }
            }
        }

        return builder.build()
    }
}
