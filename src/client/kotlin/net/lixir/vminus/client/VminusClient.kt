package net.lixir.vminus.client

import net.fabricmc.api.ClientModInitializer
import net.lixir.vminus.Vminus.Companion.ID
import net.lixir.vminus.client.registry.VClientRegistry
import net.lixir.vminus.client.definition.render.VminusRenderDefinitionGroupProvider
import net.lixir.vminus.client.definition.datagen.VminusDataGenDefinitionGroupProvider
import net.lixir.vminus.client.definition.render.RenderDefinitionRegistry
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionRegistry
import net.lixir.vminus.client.network.VminusClientNetwork
import net.lixir.vminus.client.render.block.VminusBlockRenderLayerProvider
import net.lixir.vminus.client.util.VminusTinting

class VminusClient : ClientModInitializer {
    override fun onInitializeClient() {
        VminusClientNetwork.initialize()
        VminusTinting.initialize()
        VminusBlockRenderLayerProvider(ID)
    }

    companion object {
        val DATAGEN_DEFINITION_REGISTRY = DataGenDefinitionRegistry(ID, VminusDataGenDefinitionGroupProvider())
        val RENDER_DEFINITION_REGISTRY = RenderDefinitionRegistry(ID, VminusRenderDefinitionGroupProvider()).apply {
            addProvider(VminusBlockRenderLayerProvider(ID))
        }

        val CLIENT_REGISTRY: VClientRegistry = VClientRegistry(ID)
    }
}
