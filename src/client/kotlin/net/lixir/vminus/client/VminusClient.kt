package net.lixir.vminus.client

import net.fabricmc.api.ClientModInitializer
import net.lixir.vminus.Vminus
import net.lixir.vminus.Vminus.Companion.ID
import net.lixir.vminus.client.registry.VClientRegistry
import net.lixir.vminus.client.definition.render.VminusBlockRenderDefinitionProvider
import net.lixir.vminus.client.definition.render.RenderDefinitionRegistry
import net.lixir.vminus.client.network.VminusClientNetwork
import net.lixir.vminus.client.render.block.BlockRenderLayerProvider
import net.lixir.vminus.client.util.VminusTinting

class VminusClient : ClientModInitializer {
    override fun onInitializeClient() {
        DEFINITION_REGISTRY.addDefinitionProvider(VminusBlockRenderDefinitionProvider())
        DEFINITION_REGISTRY.generateDefinitions(Vminus.REGISTRY)
        DEFINITION_REGISTRY.addSimpleProvider(BlockRenderLayerProvider(ID))
        VminusClientNetwork.initialize()
        VminusTinting.initialize()
    }

    companion object {
        val DEFINITION_REGISTRY = RenderDefinitionRegistry(ID)
        val REGISTRY = VClientRegistry(ID)
    }
}
