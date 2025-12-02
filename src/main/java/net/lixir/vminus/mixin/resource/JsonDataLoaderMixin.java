package net.lixir.vminus.mixin.resource;


import net.lixir.vminus.duck.VminusJsonDataLoaderDuck;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.JsonDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(JsonDataLoader.class)
public abstract class JsonDataLoaderMixin implements VminusJsonDataLoaderDuck {
    @Unique
    private DynamicRegistryManager.Immutable vminus$dynamicRegistry;

    public DynamicRegistryManager.Immutable getDynamicRegistry() {
        return vminus$dynamicRegistry;
    }

    public void setDynamicRegistry(DynamicRegistryManager.Immutable registryManager) {
        this.vminus$dynamicRegistry = registryManager;
    }
}
