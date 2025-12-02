package net.lixir.vminus.duck;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Map;

public interface TagManagerLoaderDuck {

    default <T> Map<Identifier, Collection<RegistryEntry<T>>> getTagsForRegistry(RegistryKey<? extends Registry<T>> registryKey) {
        return null;
    }

    default <T> boolean isInTag(RegistryKey<? extends Registry<T>> registryKey, Identifier tagId, T value) {
        return false;
    }
}
