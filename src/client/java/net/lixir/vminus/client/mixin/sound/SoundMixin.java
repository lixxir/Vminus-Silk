package net.lixir.vminus.client.mixin.sound;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.client.audio.OpusAudioResources;
import net.minecraft.client.sound.Sound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Sound.class)
public abstract class SoundMixin {
    @Shadow
    @Final
    private Identifier id;

    @ModifyReturnValue(method = "getLocation", at = @At("RETURN"))
    public Identifier detour$fixOpusPath(Identifier original) {
        if (id.getPath().endsWith(".opus")) {
            String path = id.getPath();
            path = path.substring(0, path.indexOf(".opus"));
            String namespace = id.getNamespace();
            Identifier opusLocation = Identifier.of(namespace, path);
            return OpusAudioResources.SOUND_LISTER_OPUS.toResourcePath(opusLocation);
        }
        return original;
    }
}
