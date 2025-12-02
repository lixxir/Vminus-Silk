package net.lixir.vminus.serialization

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.lixir.vminus.serialization.VminusCodecUtil.enumCodec
import net.lixir.vminus.serialization.VminusCodecUtil.enumPacketCodec
import net.minecraft.block.Block
import net.minecraft.component.Component
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.UseAction
import org.joml.Vector3f

object VminusPacketCodecs {

    val STATUS_EFFECT_CATEGORY: PacketCodec<RegistryByteBuf, StatusEffectCategory> = enumPacketCodec(StatusEffectCategory::class.java)
    val USE_ACTION: PacketCodec<RegistryByteBuf, UseAction> = enumPacketCodec(UseAction::class.java)
    val EQUIPMENT_SLOT: PacketCodec<RegistryByteBuf, EquipmentSlot> = enumPacketCodec(EquipmentSlot::class.java)
    val SOUND_EVENT: PacketCodec<RegistryByteBuf, SoundEvent> = PacketCodec.ofStatic(
        { buf, sound ->
            val id = Registries.SOUND_EVENT.getId(sound)
            buf.writeIdentifier(id)
        },
        { buf ->
            val id = buf.readIdentifier()
            Registries.SOUND_EVENT.get(id) ?: SoundEvents.BLOCK_STONE_BREAK
        }
    )
    val BLOCK: PacketCodec<RegistryByteBuf, Block> = PacketCodec.ofStatic(
        { buf, block ->
            buf.writeIdentifier(Registries.BLOCK.getId(block))
        },
        { buf ->
            val id = buf.readIdentifier()
            Registries.BLOCK.get(id)
        }
    )
    val BLOCK_SOUND_GROUP: PacketCodec<RegistryByteBuf, BlockSoundGroup> = PacketCodec.ofStatic(
        { buf, group ->
            buf.writeFloat(group.pitch)
            buf.writeFloat(group.volume)
            SOUND_EVENT.encode(buf, group.breakSound)
            SOUND_EVENT.encode(buf, group.stepSound)
            SOUND_EVENT.encode(buf, group.placeSound)
            SOUND_EVENT.encode(buf, group.hitSound)
            SOUND_EVENT.encode(buf, group.fallSound)
        },
        { buf ->
            val pitch = buf.readFloat()
            val volume = buf.readFloat()
            val breakSound = SOUND_EVENT.decode(buf)
            val stepSound = SOUND_EVENT.decode(buf)
            val placeSound = SOUND_EVENT.decode(buf)
            val hitSound = SOUND_EVENT.decode(buf)
            val fallSound = SOUND_EVENT.decode(buf)
            BlockSoundGroup(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
        }
    )
    val HEX_COLOR: PacketCodec<RegistryByteBuf, Int> = PacketCodec.ofStatic(
        { buf, value ->
            buf.writeInt(value and 0xFFFFFF)
        },
        { buf ->
            buf.readInt() and 0xFFFFFF
        }
    )
}

