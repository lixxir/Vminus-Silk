package net.lixir.vminus.mixin.server;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.data.server.bans.BannedRecipeManager;
import net.lixir.vminus.vision.VisionManagerLoader;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.command.CommandManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(DataPackContents.class)
public abstract class DatapackContentsMixin {


    @Shadow
    @Final
    private TagManagerLoader registryTagManager;

    @Shadow
    @Final
    private RecipeManager recipeManager;

    @Inject(method = "reload", at = @At("HEAD"))
    private static void vminus$onReloadStart(ResourceManager manager, CombinedDynamicRegistries<ServerDynamicRegistryType> dynamicRegistries, FeatureSet enabledFeatures, CommandManager.RegistrationEnvironment environment, int functionPermissionLevel, Executor prepareExecutor, Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<DataPackContents>> cir) {
        Vision.Companion.clearAllVisions();
    }

    @Inject(method = "<init>", at = @At("TAIL"))

    private void vminus$attachRegistryAccess(DynamicRegistryManager.Immutable registryManager, FeatureSet flags,
                                             CommandManager.RegistrationEnvironment environment, int funcLevel,
                                             CallbackInfo ci) {
        this.recipeManager.setDynamicRegistry(registryManager);
    }

    @ModifyReturnValue(method = "getContents", at = @At("RETURN"))
    private @NotNull List<ResourceReloader> vMinus$addListeners(List<ResourceReloader> original) {
        List<ResourceReloader> vanillaListeners = new ArrayList<>(original);
        vanillaListeners.remove(registryTagManager);
        List<ResourceReloader> newListeners = new ArrayList<>();
        newListeners.add(BannedRecipeManager.INSTANCE);
        newListeners.add(registryTagManager);
        newListeners.add(new VisionManagerLoader<>(VisionTypes.INSTANCE.getITEM(), Registries.ITEM, registryTagManager));
        newListeners.add(new VisionManagerLoader<>(VisionTypes.INSTANCE.getBLOCK(), Registries.BLOCK, registryTagManager));
        newListeners.add(new VisionManagerLoader<>(VisionTypes.INSTANCE.getENTITY_TYPE(), Registries.ENTITY_TYPE, registryTagManager));
        newListeners.add(new VisionManagerLoader<>(VisionTypes.INSTANCE.getITEM_GROUP(), Registries.ITEM_GROUP, registryTagManager));
        newListeners.add(new VisionManagerLoader<>(VisionTypes.INSTANCE.getSTATUS_EFFECT(), Registries.STATUS_EFFECT, registryTagManager));
        newListeners.addAll(vanillaListeners);
        return newListeners;
    }
}
