package net.lixir.vminus.mixin.registry.tag;

import net.lixir.vminus.duck.TagManagerLoaderDuck;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(TagManagerLoader.class)
public class TagManagerLoaderMixin implements TagManagerLoaderDuck {
    @Shadow
    private List<TagManagerLoader.RegistryTags<?>> registryTags;

    @Override
    public <T> Map<Identifier, Collection<RegistryEntry<T>>> getTagsForRegistry(RegistryKey<? extends Registry<T>> registryKey) {
        for (TagManagerLoader.RegistryTags<?> registryTags : registryTags) {
            if (registryTags.key().equals(registryKey)) {
                @SuppressWarnings("unchecked")
                TagManagerLoader.RegistryTags<T> typed = (TagManagerLoader.RegistryTags<T>) registryTags;
                return typed.tags();
            }
        }
        return Map.of();
    }

    @Override
    public <T> boolean isInTag(RegistryKey<? extends Registry<T>> registryKey, Identifier tagId, T value) {
        Map<Identifier, Collection<RegistryEntry<T>>> tags = getTagsForRegistry(registryKey);
        Collection<RegistryEntry<T>> entries = tags.get(tagId);
        if (entries == null)
            return false;
        for (RegistryEntry<T> entry : entries) {
            if (entry.value().equals(value))
                return true;
        }
        return false;
    }
}
