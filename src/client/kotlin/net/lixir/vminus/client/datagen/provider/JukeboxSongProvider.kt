package net.lixir.vminus.client.datagen.provider

import com.mojang.serialization.JsonOps
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.util.Identifier
import net.minecraft.block.jukebox.JukeboxSong
import net.minecraft.registry.RegistryKey
import java.util.concurrent.CompletableFuture
import java.util.*

abstract class JukeboxSongProvider(private val output: FabricDataOutput) : DataProvider {

    val modId: String = output.modId
    private val songs: MutableMap<Identifier, JukeboxSong> = HashMap()

    protected abstract fun addSongs()

    protected fun add(identifier: Identifier, jukeboxSong: JukeboxSong): JukeboxSong {
        songs[identifier] = jukeboxSong
        return jukeboxSong
    }

    protected fun add(key: RegistryKey<JukeboxSong>, jukeboxSong: JukeboxSong): JukeboxSong {
        return add(key.value, jukeboxSong)
    }

    protected fun add(location: String, jukeboxSong: JukeboxSong): JukeboxSong {
        return add(Identifier.of(modId, location), jukeboxSong)
    }

    override fun run(writer: DataWriter): CompletableFuture<*> {
        addSongs()
        val futures = mutableListOf<CompletableFuture<*>>()

        for ((id, song) in songs) {
            val path = output.path.resolve("data/${id.namespace}/jukebox_song/${id.path}.json")
            val json = JukeboxSong.CODEC.encodeStart(JsonOps.INSTANCE, song)
                .getOrThrow { ex -> IllegalStateException(ex.toString()) }
            futures.add(DataProvider.writeToPath(writer, json, path))
        }

        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    override fun getName(): String = "JukeboxSong Provider"
}
