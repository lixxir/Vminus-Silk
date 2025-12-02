package net.lixir.vminus.vision.property

import com.mojang.serialization.Codec
import net.lixir.vminus.serialization.VminusCodecs
import net.lixir.vminus.serialization.VminusPacketCodecs
import net.lixir.vminus.vision.VisionTypes
import net.lixir.vminus.vision.property.VisionPropertyRegistry.register
import net.minecraft.block.Block
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object BlockVisionProperties {
    val VELOCITY_MULTIPLIER: VisionProperty<Float> =
        register(VisionTypes.BLOCK, VisionProperty("velocity_multiplier", Codec.FLOAT, PacketCodecs.FLOAT))
    val JUMP_VELOCITY_MULTIPLIER: VisionProperty<Float> =
        register(VisionTypes.BLOCK, VisionProperty("jump_velocity_factor", Codec.FLOAT, PacketCodecs.FLOAT))
    val BLAST_RESISTANCE: VisionProperty<Float> =
        register(VisionTypes.BLOCK, VisionProperty("blast_resistance", Codec.FLOAT, PacketCodecs.FLOAT))
    val HARDNESS: VisionProperty<Float> =
        register(VisionTypes.BLOCK, VisionProperty("hardness", Codec.FLOAT, PacketCodecs.FLOAT))
    val SLIPPERINESS: VisionProperty<Float> =
        register(VisionTypes.BLOCK, VisionProperty("slipperiness", Codec.FLOAT, PacketCodecs.FLOAT))
    val BAN: VisionProperty<Boolean> =
        register(VisionTypes.BLOCK, VisionProperty("ban", Codec.BOOL, PacketCodecs.BOOL))
    val REPLACE: VisionProperty<Block> =
        register(VisionTypes.BLOCK, VisionProperty("replace", VminusCodecs.BLOCK, VminusPacketCodecs.BLOCK))
    val EMISSIVE_LIGHTING: VisionProperty<Boolean> =
        register(VisionTypes.BLOCK, VisionProperty("emissive_lighting", Codec.BOOL, PacketCodecs.BOOL))
    val OPAQUE: VisionProperty<Boolean> =
        register(VisionTypes.BLOCK, VisionProperty("opaque", Codec.BOOL, PacketCodecs.BOOL))
    val SOLID: VisionProperty<Boolean> =
        register(VisionTypes.BLOCK, VisionProperty("solid", Codec.BOOL, PacketCodecs.BOOL))
    val LUMINANCE: VisionProperty<Int> =
        register(VisionTypes.BLOCK, VisionProperty("luminance", Codec.INT, PacketCodecs.VAR_INT))
    val LOOT_TABLE: VisionProperty<Identifier> =
        register(VisionTypes.BLOCK, VisionProperty("loot_table", Identifier.CODEC, Identifier.PACKET_CODEC))
    val SOUND: VisionProperty<BlockSoundGroup> =
        register(VisionTypes.BLOCK, VisionProperty("sound", VminusCodecs.BLOCK_SOUND_GROUP, VminusPacketCodecs.BLOCK_SOUND_GROUP))

    fun initialize() {

    }
}