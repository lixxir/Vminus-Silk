package net.lixir.vminus.client.datagen.provider

import com.mojang.serialization.JsonOps
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.entity.decoration.painting.PaintingVariant
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture
import java.util.*

abstract class PaintingVariantProvider(private val output: FabricDataOutput) : DataProvider {

    val modId: String = output.modId
    private val variants: MutableMap<Identifier, PaintingVariant> = HashMap()

    protected abstract fun addVariants()

    protected fun add(identifier: Identifier, variant: PaintingVariant): PaintingVariant {
        variants[identifier] = variant
        return variant
    }

    protected fun add(key: RegistryKey<PaintingVariant>, variant: PaintingVariant): PaintingVariant {
        return add(key.value, variant)
    }

    protected fun add(location: String, variant: PaintingVariant): PaintingVariant {
        return add(Identifier.of(modId, location), variant)
    }

    override fun run(writer: DataWriter): CompletableFuture<*> {
        addVariants()
        val futures = mutableListOf<CompletableFuture<*>>()

        for ((id, variant) in variants) {
            val path = output.path.resolve("data/${id.namespace}/painting_variant/${id.path}.json")
            val json = PaintingVariant.CODEC.encodeStart(JsonOps.INSTANCE, variant)
                .getOrThrow { ex -> IllegalStateException("Failed to encode PaintingVariant $id: $ex") }
            futures.add(DataProvider.writeToPath(writer, json, path))
        }

        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    override fun getName(): String = "PaintingVariant Provider"
}
