package net.lixir.vminus.client.mixin.sound;


import net.lixir.vminus.client.audio.OpusAudioResources;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(targets = "net.minecraft.client.sound.SoundManager$SoundList")
public class SoundManagerSoundsListMixin {
    @Shadow
    private Map<Identifier, Resource> foundSounds;

    @Inject(method = "findSounds", at = @At("TAIL"))
    private void detour$includeOpusFilesEarly(ResourceManager resourceManager, CallbackInfo ci) {
        Map<Identifier, Resource> opus = OpusAudioResources.SOUND_LISTER_OPUS.findResources(resourceManager);
        foundSounds.putAll(opus);
    }
}
