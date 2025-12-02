package net.lixir.vminus.vision.property

import net.lixir.vminus.vision.VisionType
import org.jetbrains.annotations.Contract
import java.util.*

object VisionPropertyRegistry {
    private val REGISTRY: MutableMap<VisionType<*>, MutableList<VisionProperty<*>>> = HashMap()
    private val PROPERTY_BY_ID: MutableMap<Key, VisionProperty<*>> = HashMap()

    fun <T, V> register(visionType: VisionType<T>, property: VisionProperty<V>): VisionProperty<V> {
        REGISTRY.computeIfAbsent(visionType) { ArrayList() }.add(property)
        PROPERTY_BY_ID[Key(visionType, property.id)] = property
        return property
    }

    fun <T> get(visionType: VisionType<T>?, id: String?): VisionProperty<T> {
        return PROPERTY_BY_ID[Key(visionType, id)] as VisionProperty<T>
    }

    fun fromVisionType(visionType: VisionType<*>?): List<VisionProperty<*>> {
        val list: List<*> = REGISTRY.getOrDefault(visionType, listOf())
        return Collections.unmodifiableList(list) as List<VisionProperty<*>>
    }

    @JvmRecord
    private data class Key(val type: VisionType<*>?, val id: String?)
}
