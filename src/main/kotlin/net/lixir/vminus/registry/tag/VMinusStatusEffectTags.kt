package net.lixir.vminus.registry.tag

import net.lixir.vminus.Vminus.Companion.vminusIdentifier
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object VMinusStatusEffectTags {
    val IS_RESISTANCE: TagKey<StatusEffect> = of("is_resistance")

    private fun of(name: String): TagKey<StatusEffect> {
        return TagKey.of(RegistryKeys.STATUS_EFFECT, vminusIdentifier(name))
    }
}