package net.lixir.vminus.registry.tag

import net.lixir.vminus.Vminus.Companion.vminusIdentifier
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object VminusDamageTypeTags {
    val BLUNT_DAMAGE: TagKey<DamageType> = of("protection/blunt")
    val BLAST_DAMAGE: TagKey<DamageType> = of("protection/blast")
    val FALL_DAMAGE: TagKey<DamageType> = of("protection/fall")
    val FIRE_DAMAGE: TagKey<DamageType> = of("protection/fire")
    val MAGIC_DAMAGE: TagKey<DamageType> = of("protection/magic")

    private fun of(name: String): TagKey<DamageType> {
        return TagKey.of(RegistryKeys.DAMAGE_TYPE, vminusIdentifier(name))
    }
}