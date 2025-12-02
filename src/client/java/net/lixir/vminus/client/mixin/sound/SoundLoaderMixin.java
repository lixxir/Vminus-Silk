package net.lixir.vminus.client.mixin.sound;


import net.lixir.vminus.Vminus;
import net.lixir.vminus.client.audio.OggOpusAudioStream;
import net.minecraft.client.sound.AudioStream;
import net.minecraft.client.sound.RepeatingAudioStream;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.client.sound.StaticSound;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Mixin(SoundLoader.class)
public class SoundLoaderMixin {
    @Shadow
    @Final
    private ResourceFactory resourceFactory;
    @Shadow
    @Final
    private Map<Identifier, CompletableFuture<StaticSound>> loadedSounds;

    @Inject(method = "loadStreamed", at = @At("RETURN"), cancellable = true)
    private void detour$injectOpusSupport(@NotNull Identifier id, boolean repeatInstantly, CallbackInfoReturnable<CompletableFuture<AudioStream>> cir) {
        if (id.getPath().endsWith(".opus")) {
            cir.setReturnValue(CompletableFuture.supplyAsync(() -> {
                try {
                    InputStream stream = resourceFactory.open(id);
                    if (repeatInstantly) {
                        return new RepeatingAudioStream(OggOpusAudioStream::new, stream);
                    } else {
                        return new OggOpusAudioStream(stream);
                    }
                } catch (IOException e) {
                    throw new CompletionException("Failed to load Opus stream: " + id, e);
                }
            }, Util.getMainWorkerExecutor()));
        }
    }

    @Inject(method = "loadStatic(Lnet/minecraft/util/Identifier;)Ljava/util/concurrent/CompletableFuture;",
            at = @At("RETURN"), cancellable = true)
    public void supportOpus(@NotNull Identifier id, CallbackInfoReturnable<CompletableFuture<StaticSound>> cir) {
        if (id.getPath().endsWith(".opus")) {
            this.loadedSounds.remove(id);
            cir.setReturnValue(this.loadedSounds.computeIfAbsent(id, loc ->
                    CompletableFuture.supplyAsync(() -> {
                        try (
                                InputStream input = resourceFactory.open(loc);
                                OggOpusAudioStream stream = new OggOpusAudioStream(input)
                        ) {
                            ByteBuffer buffer = stream.readAll();
                            return new StaticSound(buffer, stream.getFormat());
                        } catch (IOException e) {
                            Vminus.LOGGER.error("IO error while loading Opus stream {}: {}", loc, e.getMessage(), e);
                            throw new CompletionException(e);
                        } catch (Throwable t) {
                            Vminus.LOGGER.error("Unexpected error while loading Opus stream {}: {}", loc, t.getMessage(), t);
                            throw new CompletionException(t);
                        }
                    }, Util.getMainWorkerExecutor())

            ));
        }
    }
}
