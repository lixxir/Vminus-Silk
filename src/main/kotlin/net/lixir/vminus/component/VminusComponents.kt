package net.lixir.vminus.component

import net.lixir.vminus.Vminus.Companion.vminusIdentifier
import net.minecraft.entity.player.PlayerEntity
import org.ladysnake.cca.api.v3.component.ComponentKey
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer

class VminusComponents : EntityComponentInitializer {
    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(
            CAPE
        ) { player: PlayerEntity -> CapeComponentImpl(player) }
    }

    companion object {
        val CAPE: ComponentKey<CapeComponent> = ComponentRegistryV3.INSTANCE.getOrCreate(
            vminusIdentifier("cape"),
            CapeComponent::class.java
        )
    }
}