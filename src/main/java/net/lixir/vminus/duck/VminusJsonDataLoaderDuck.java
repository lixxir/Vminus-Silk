package net.lixir.vminus.duck;


import net.minecraft.registry.DynamicRegistryManager;
import org.jetbrains.annotations.Nullable;

public interface VminusJsonDataLoaderDuck {
    @Nullable
    default DynamicRegistryManager.Immutable getDynamicRegistry() {
        return null;
    }

    default void setDynamicRegistry(DynamicRegistryManager.Immutable registryManager) {

    }
}
