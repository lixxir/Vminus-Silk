package net.lixir.vminus.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import net.lixir.vminus.vision.Vision;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {


    @Inject(method = "onPlayerConnect", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V",
            ordinal = 3, shift = At.Shift.AFTER))
    private void vminus$playerConnected(CallbackInfo ci, @Local(argsOnly = true) ServerPlayerEntity serverPlayerEntity) {
        Vision.Companion.sync(serverPlayerEntity);
    }

    @Inject(method = "onDataPacksReloaded", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", shift = At.Shift.AFTER))
    private void vminus$onDatapacksReloadedForEachPlayer(CallbackInfo ci, @Local ServerPlayerEntity serverPlayerEntity) {
        Vision.Companion.sync(serverPlayerEntity);
    }
}
