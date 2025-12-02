package net.lixir.vminus.vision

import net.lixir.vminus.duck.VisionDuck
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.UnmodifiableView
import java.util.*
import java.util.function.BiConsumer

class VisionType<T> {
    val id: String
    val multiList: String
    val registry: Registry<out T>
    val visionSetter: BiConsumer<Any, Identifier>
    private val visions: MutableMap<Identifier, Vision> = HashMap()

    constructor(id: String, registry: Registry<out T>) {
        this.id = id
        this.multiList = id + "s"
        this.registry = registry
        this.visionSetter =
            BiConsumer { obj: Any, id2: Identifier? -> (obj as VisionDuck).`vminus$setVisionIdentifier`(id2) }
    }

    constructor(id: String, multiList: String, registry: Registry<out T>) {
        this.id = id
        this.multiList = multiList
        this.registry = registry
        this.visionSetter =
            BiConsumer { obj: Any, id2: Identifier? -> (obj as VisionDuck).`vminus$setVisionIdentifier`(id2) }
    }

    fun getVisions(): @UnmodifiableView MutableMap<Identifier, Vision> {
        return Collections.unmodifiableMap(visions)
    }

    fun <E : Any> applyVision(target: E, id: Identifier) {
        visionSetter.accept(target, id)
    }

    fun resetVisionTypes() {
        for (value in registry) {
            (value as VisionDuck).`vminus$setVisionIdentifier`(null)
        }
    }

    fun putVision(id: Identifier, vision: Vision) {
        visions[id] = vision
    }

    val directory: String get() = "visions/$multiList"

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null || other.javaClass != this.javaClass) return false
        val that = other as VisionType<*>
        return this.id == that.id &&
                this.multiList == that.multiList &&
                this.registry == that.registry &&
                this.visionSetter == that.visionSetter
    }

    override fun hashCode(): Int {
        return Objects.hash(id, multiList, registry, visionSetter)
    }

    override fun toString(): String {
        return "VisionType[id=$id]"
    }

    companion object {
        fun resetAllVisionTypes() {
            for (visionType in VisionTypes.all) {
                visionType?.resetVisionTypes()
                visionType?.visions?.clear()
            }
        }
    }
}
