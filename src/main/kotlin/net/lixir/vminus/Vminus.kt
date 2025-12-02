package net.lixir.vminus

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.lixir.vminus.block.VminusBlocks
import net.lixir.vminus.item.VminusItems
import net.lixir.vminus.network.VminusNetwork
import net.lixir.vminus.registry.VRegistry
import net.lixir.vminus.vision.VisionTypes
import net.lixir.vminus.vision.property.*
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Vminus : ModInitializer {
    override fun onInitialize() {
        VisionTypes.initialize()
        ItemVisionProperties.initialize()
        BlockVisionProperties.initialize()
        ItemGroupVisionProperties.initialize()
        EntityTypeVisionProperties.initialize()
        StatusEffectVisionProperties.initialize()
        VminusNetwork.initialize()
        VminusItems.initialize()
        VminusBlocks.initialize()
    }

    companion object {
        const val ID = "vminus"
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger(ID)
        val REGISTRY = VRegistry(ID)

        fun devLogger(loggerInput: String?) {
            if (!FabricLoader.getInstance().isDevelopmentEnvironment) {
                return
            }

            LOGGER.info("DEV - [ $loggerInput ]")
        }

        fun vminusIdentifier(path: String): Identifier = Identifier.of(ID, path)
    }
}
