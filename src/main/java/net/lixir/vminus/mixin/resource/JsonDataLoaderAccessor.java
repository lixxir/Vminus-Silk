package net.lixir.vminus.mixin.resource;

import com.google.gson.Gson;
import net.minecraft.resource.JsonDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JsonDataLoader.class)
public interface JsonDataLoaderAccessor {
    @Accessor("gson")
    Gson getGson();
}
