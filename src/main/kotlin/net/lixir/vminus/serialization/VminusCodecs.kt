package net.lixir.vminus.serialization

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.lixir.vminus.serialization.VminusCodecUtil.enumCodec
import net.minecraft.block.Block
import net.minecraft.component.Component
import net.minecraft.component.ComponentType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.UseAction
import org.ladysnake.cca.api.v3.component.ComponentRegistry
import org.spongepowered.asm.mixin.Dynamic
import com.mojang.datafixers.util.Pair as Pair1


object VminusCodecs {
    val BLOCK_SOUND_GROUP: Codec<BlockSoundGroup> = RecordCodecBuilder.create { instance ->
        instance.group(
            Codec.FLOAT.optionalFieldOf("pitch", 1f).forGetter { it.pitch },
            Codec.FLOAT.optionalFieldOf("level", 1f).forGetter { it.volume },
            Identifier.CODEC.optionalFieldOf("break", Registries.SOUND_EVENT.getId(SoundEvents.BLOCK_STONE_BREAK))
                .xmap({ id -> Registries.SOUND_EVENT.get(id) }, { sound -> Registries.SOUND_EVENT.getId(sound) })
                .forGetter { it.breakSound },
            Identifier.CODEC.optionalFieldOf("step", Registries.SOUND_EVENT.getId(SoundEvents.BLOCK_STONE_STEP))
                .xmap({ id -> Registries.SOUND_EVENT.get(id) }, { sound -> Registries.SOUND_EVENT.getId(sound) })
                .forGetter { it.stepSound },
            Identifier.CODEC.optionalFieldOf("place", Registries.SOUND_EVENT.getId(SoundEvents.BLOCK_STONE_PLACE))
                .xmap({ id -> Registries.SOUND_EVENT.get(id) }, { sound -> Registries.SOUND_EVENT.getId(sound) })
                .forGetter { it.placeSound },
            Identifier.CODEC.optionalFieldOf("hit", Registries.SOUND_EVENT.getId(SoundEvents.BLOCK_STONE_HIT))
                .xmap({ id -> Registries.SOUND_EVENT.get(id) }, { sound -> Registries.SOUND_EVENT.getId(sound) })
                .forGetter { it.hitSound },
            Identifier.CODEC.optionalFieldOf("fall", Registries.SOUND_EVENT.getId(SoundEvents.BLOCK_STONE_FALL))
                .xmap({ id -> Registries.SOUND_EVENT.get(id) }, { sound -> Registries.SOUND_EVENT.getId(sound) })
                .forGetter { it.fallSound }
        ).apply(instance) { pitch, volume, breakSound, stepSound, placeSound, hitSound, fallSound ->
            BlockSoundGroup(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
        }
    }

    val BLOCK: Codec<Block> = Identifier.CODEC.xmap(
        { id -> Registries.BLOCK.get(id) },
        { block -> Registries.BLOCK.getId(block) }
    )
    val SOUND_EVENT: Codec<SoundEvent> = Identifier.CODEC.xmap(
        { id -> Registries.SOUND_EVENT.get(id) },
        { sound -> Registries.SOUND_EVENT.getId(sound) }
    )
    val HEX_COLOR: Codec<Int> = Codec.STRING.xmap(
        { str ->
            val hex = str.removePrefix("#")
            try {
                hex.toInt(16)
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid hex color: $str")
            }
        },
        { value -> "#%06X".format(value and 0xFFFFFF) }
    )
    val STATUS_EFFECT_CATEGORY: Codec<StatusEffectCategory> = enumCodec(StatusEffectCategory::class.java)
    val USE_ACTION: Codec<UseAction> = enumCodec(UseAction::class.java)
    val EQUIPMENT_SLOT: Codec<EquipmentSlot> = enumCodec(EquipmentSlot::class.java)
}