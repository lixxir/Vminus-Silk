package net.lixir.vminus.mixin.server.world;

import net.lixir.vminus.VminusHooks;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerEntityManager.class)
public abstract class ServerEntityManagerMixin<T extends EntityLike> {
    @Inject(method = "addEntity(Lnet/minecraft/world/entity/EntityLike;Z)Z", at = @At("HEAD"), cancellable = true)
    private void vminus$addEntity(T entity, boolean existing, CallbackInfoReturnable<Boolean> cir) {
        if (VminusHooks.onEntityAdded(entity))
            cir.setReturnValue(false);
    }
}
