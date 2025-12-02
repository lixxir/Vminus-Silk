package net.lixir.vminus.registry.tag

import net.lixir.vminus.Vminus.Companion.vminusIdentifier
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object VminusEntityTypeTags {
    val ZOMBIES: TagKey<EntityType<*>> = of("zombies")
    val BANNED: TagKey<EntityType<*>> = of("banned")
    val CREATIVE_ONLY: TagKey<EntityType<*>> = of("creative_only")

    private fun of(name: String): TagKey<EntityType<*>> {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, vminusIdentifier(name))
    }
}