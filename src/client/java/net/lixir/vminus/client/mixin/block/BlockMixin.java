package net.lixir.vminus.client.mixin.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lixir.vminus.client.api.extensions.VminusClientBlockExtensions;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
@Environment(EnvType.CLIENT)
public class BlockMixin implements VminusClientBlockExtensions {

}
