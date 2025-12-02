package net.lixir.vminus.client.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.lixir.vminus.Vminus
import net.lixir.vminus.network.payload.ResetVisionsS2CPayload
import net.lixir.vminus.network.payload.VisionsS2CPayload
import net.lixir.vminus.vision.Vision.Companion.clearAllVisions
import net.lixir.vminus.vision.VisionTypes.get

object VminusClientNetwork {
    fun initialize() {
        ClientPlayNetworking.registerGlobalReceiver(
            ResetVisionsS2CPayload.ID
        ) { _: ResetVisionsS2CPayload?, _: ClientPlayNetworking.Context? -> clearAllVisions() }
        ClientPlayNetworking.registerGlobalReceiver(VisionsS2CPayload.ID) { payload, _ ->
            val visionType = get(payload!!.visionTypeId)
            if (visionType == null) {
                Vminus.LOGGER.warn("VisionType not found for id: {}", payload.visionTypeId)
                return@registerGlobalReceiver
            }

            for ((ids, vision) in payload.groupedVisions) {
                for (id in ids) {
                    try {
                        visionType.putVision(id, vision)
                        val target = visionType.registry[id]
                        if (target != null)
                            visionType.visionSetter.accept(target, id)
                    } catch (ex: Exception) {
                        Vminus.LOGGER.error("Error syncing vision for id {}: ", id, ex)
                    }
                }
            }
        }
    }
}
