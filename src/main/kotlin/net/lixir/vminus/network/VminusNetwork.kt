package net.lixir.vminus.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.lixir.vminus.network.payload.ResetVisionsS2CPayload
import net.lixir.vminus.network.payload.VisionsS2CPayload

object VminusNetwork {
    fun initialize() {
        PayloadTypeRegistry.playS2C().register(ResetVisionsS2CPayload.ID, ResetVisionsS2CPayload.CODEC)
        PayloadTypeRegistry.playS2C().register(VisionsS2CPayload.ID, VisionsS2CPayload.CODEC)
    }
}
