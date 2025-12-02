package net.lixir.vminus.vision

import com.google.common.collect.ImmutableMap
import com.google.gson.JsonArray
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.lixir.vminus.duck.VisionDuck
import net.lixir.vminus.network.payload.ResetVisionsS2CPayload
import net.lixir.vminus.network.payload.VisionsS2CPayload
import net.lixir.vminus.vision.condition.VisionContext
import net.lixir.vminus.vision.property.VisionProperty
import net.lixir.vminus.vision.property.VisionPropertyRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class Vision private constructor(
    private val values: ImmutableMap<String, Array<VisionValue<*>>?>
) {

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(id: String, context: VisionContext? = null, fallback: T? = null): T? {
        val props = values[id] as? Array<VisionValue<T>> ?: return fallback
        return props.firstOrNull { true }?.getValues()?.get(0) ?: fallback
    }

    fun <T> getValue(property: VisionProperty<T>, context: VisionContext? = null): T? {
        return getValue(property, context, null)
    }

    fun <T> getValue(id: String, context: VisionContext? = null): T? {
        return getValue<T>(id, context, null)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValues(id: String, context: VisionContext? = null): List<T> {
        val props = values[id] as? Array<VisionValue<T>> ?: return emptyList()
        return props.flatMap { it.getValues() }
    }

    fun <T> getValues(prop: VisionProperty<T>): List<T> =
        getValues(prop.id, null)

    fun <T> getValue(prop: VisionProperty<T>, context: VisionContext? = null, fallback: T? = null): T? =
        getValue(prop.id, context, fallback)

    fun <T> getValues(prop: VisionProperty<T>, context: VisionContext? = null): List<T> =
        getValues(prop.id, context)


    override fun equals(other: Any?): Boolean =
        other is Vision && values == other.values

    override fun hashCode(): Int = values.hashCode()

    override fun toString(): String = buildString {
        append("Vision {\n")
        values.forEach { (key, props) ->
            append("  $key = [")
            append(props?.joinToString(", ") ?: "")
            append("]\n")
        }
        append("}")
    }

    @Suppress("UNCHECKED_CAST")
    fun <B : RegistryByteBuf> writeToBuffer(buf: B, type: VisionType<*>) {
        buf.writeVarInt(values.size)
        for ((key, valueArray) in values) {
            buf.writeString(key)
            val property = VisionPropertyRegistry.get(type, key)
            buf.writeVarInt(valueArray?.size ?: 0)
            valueArray?.forEach { v ->
                val innerValues = v.getValues()
                buf.writeVarInt(innerValues.size)

                innerValues.forEach { inner ->
                    (property.packetCodec as PacketCodec<B, Any?>).encode(buf, inner)
                }
            }
        }
    }


    companion object {
        private val EMPTY = Vision(ImmutableMap.of())
        private val UNIQUE_VISION_POOL = mutableSetOf<Vision>()

        @Suppress("UNCHECKED_CAST")
        fun <B : RegistryByteBuf> readVisionFromBuffer(buf: B, type: VisionType<*>): Vision {
            val size = buf.readVarInt()
            val map = mutableMapOf<String, Array<VisionValue<*>>>()

            repeat(size) {
                val key = buf.readString()
                val property = VisionPropertyRegistry.get(type, key)
                val count = buf.readVarInt()
                val list = mutableListOf<VisionValue<*>>()

                repeat(count) {
                    val innerCount = buf.readVarInt()
                    val innerValues = mutableListOf<Any?>()

                    repeat(innerCount) {
                        val value = (property.packetCodec as PacketCodec<B, Any?>).decode(buf)
                        innerValues.add(value)
                    }

                    list.add(VisionValue(innerValues.toMutableList()))
                }

                map[key] = list.toTypedArray()
            }

            return Vision(ImmutableMap.copyOf(map))
        }

        fun sync(player: ServerPlayerEntity) {
            ServerPlayNetworking.send(player, ResetVisionsS2CPayload())

            VisionManagerLoader.visionManagerLoaders.toList().forEach { manager ->
                val visionType = manager.visionType
                val grouped = mutableMapOf<Vision, MutableList<Identifier>>()
                val existingVisions = mutableListOf<Vision>()

                manager.visionEntries.toList().forEach { (id, vision) ->
                    val match = existingVisions.find { it == vision }
                    if (match != null) {
                        grouped[match]!!.add(id)
                    } else {
                        grouped[vision] = mutableListOf(id)
                        existingVisions.add(vision)
                    }
                }

                val pairs = grouped.map { Pair(it.value.toList(), it.key) }
                ServerPlayNetworking.send(player, VisionsS2CPayload(visionType.id, pairs))
            }
        }

        fun get(visionType: VisionType<*>, id: Identifier?): Vision {
            if (id ==  null)
                return EMPTY
            val visions = visionType.getVisions()
            val vision = visions.getOrDefault(id, EMPTY)
            return vision
        }


        private fun getOrAddVision(visionType: VisionType<*>, id: Identifier, vision: Vision): Vision {
            UNIQUE_VISION_POOL.find { it == vision }?.let {
                visionType.putVision(id, it)
                return it
            }
            UNIQUE_VISION_POOL.add(vision)
            visionType.putVision(id, vision)
            return vision
        }

        fun get(duck: VisionDuck): Vision {
            val id: Identifier? = duck.`vminus$getVisionIdentifier`()
            val type = duck.`vminus$getVisionType`()
            val visions = type.getVisions();
            val vision = get(type, id)
            return vision;
        }

        fun get(entity: Entity): Vision = get(entity as VisionDuck)
        fun get(item: Item): Vision = get(item as VisionDuck)
        fun get(stack: ItemStack): Vision = get(stack.item)
        fun get(block: Block): Vision = get(block as VisionDuck)
        fun get(state: BlockState): Vision = get(state.block)
        fun get(group: ItemGroup): Vision = get(group as VisionDuck)
        fun get(entityType: EntityType<*>): Vision = get(entityType as VisionDuck)
        fun get(statusEffect: StatusEffect): Vision = get(statusEffect as VisionDuck)

        fun clearAllVisions() {
            VisionManagerLoader.clearVisionManagers()
            VisionType.resetAllVisionTypes()
            UNIQUE_VISION_POOL.clear()
        }

        fun <T> fromEntry(id: Identifier, entry: VisionDataEntry<T>, type: VisionType<*>): Vision {
            val builder = ImmutableMap.builder<String, Array<VisionValue<*>>?>()
            entry.values.forEach { (propId, list) ->
                val filtered = mutableListOf<VisionValue<*>>()
                var constant: VisionValue<*>? = null
                list.forEach { value ->
                    filtered.add(value)
                    constant = when {
                        constant != null && value.priority <= constant!!.priority -> constant
                        constant == null -> value
                        constant != null -> {
                            filtered.remove(constant)
                            value
                        }
                        else -> {
                            filtered.remove(constant)
                            null
                        }
                    }
                }
                builder.put(propId, filtered.toTypedArray())
            }
            return getOrAddVision(type, id, Vision(builder.build()))
        }

        fun <T> getValue(duck: VisionDuck, prop: VisionProperty<T>): T? =
            get(duck).getValue(prop.id, VisionContext.fromDuck(duck))

        fun <T> getValue(duck: VisionDuck, prop: VisionProperty<T>, fallback: T?): T? =
            get(duck).getValue(prop.id, VisionContext.fromDuck(duck), fallback)

        fun <T> getValue(duck: VisionDuck, prop: VisionProperty<T>, context: VisionContext = VisionContext.fromDuck(duck), fallback: T? = null): T? =
            get(duck).getValue(prop.id, context, fallback)

        fun <T> getValues(duck: VisionDuck, prop: VisionProperty<T>, context: VisionContext = VisionContext.fromDuck(duck)): List<T> =
            get(duck).getValues(prop.id, context)

        fun <T> getValues(duck: VisionDuck, prop: VisionProperty<T>): List<T> =
            get(duck).getValues(prop.id, VisionContext.fromDuck(duck))

        private fun <T : Any> encodeProperty(prop: VisionProperty<T>, value: VisionValue<*>, array: JsonArray) {

        }
    }
}
