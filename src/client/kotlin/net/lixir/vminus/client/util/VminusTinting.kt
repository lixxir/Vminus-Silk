package net.lixir.vminus.client.util

import net.lixir.vminus.client.VminusClient
import net.lixir.vminus.client.render.TintTypes
import net.lixir.vminus.item.VminusItems

object VminusTinting {
    fun initialize() {
        VminusClient.CLIENT_REGISTRY.tintItems(TintTypes.FOLIAGE, VminusItems.TEST)
    }
}
