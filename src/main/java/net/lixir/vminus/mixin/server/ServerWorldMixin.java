package net.lixir.vminus.mixin.server;

import net.lixir.vminus.VminusHooks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(method = "addPlayer", at = @At(value = "HEAD"))
    private void vminus$addEntity(ServerPlayerEntity player, CallbackInfo ci) {
        VminusHooks.onEntityAdded(player);
    }
}
