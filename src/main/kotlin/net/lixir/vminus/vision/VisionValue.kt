package net.lixir.vminus.vision

import com.google.common.collect.ImmutableList
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.lixir.vminus.vision.condition.AbstractVisionCondition
import net.lixir.vminus.vision.condition.VisionContext
import net.minecraft.util.math.MathHelper
import java.util.Collections

class VisionValue<T>(
    private val values: MutableList<T>,
    priority: Short = DEFAULT_PRIORITY,
    val listable: Boolean = false
) {
   val priority: Short = MathHelper.clamp(priority.toInt(), MIN_PRIORITY.toInt(), MAX_PRIORITY.toInt()).toShort()

    override fun equals(other: Any?): Boolean =
        this === other || (other is VisionValue<*> &&
                priority == other.priority &&
                values == other.values)

    fun getValues(): List<T> {
        return Collections.unmodifiableList(values)
    }

    fun addValue(t: T) {
        if (listable) values.add(t) else values[0] = t
    }

    override fun hashCode(): Int = arrayOf(values, priority).contentHashCode()

    override fun toString(): String =
        "VisionValue[values=$values, priority=$priority]"

    companion object {
        private const val PRIORITY_TAG = "/priority"
        const val DEFAULT_PRIORITY: Short = 500
        const val MIN_PRIORITY: Short = 0
        const val MAX_PRIORITY: Short = 1000

    }
}
