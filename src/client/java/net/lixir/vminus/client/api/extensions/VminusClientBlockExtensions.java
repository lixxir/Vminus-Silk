package net.lixir.vminus.client.api.extensions;

import net.minecraft.client.input.Input;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VminusClientBlockExtensions {
    default @Nullable Input onPlayerInput(@NotNull World world, @NotNull PlayerEntity player, @NotNull Input input) {
        return null;
    }
}
