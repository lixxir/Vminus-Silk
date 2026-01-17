package net.lixir.vminus.util;

import net.minecraft.util.Identifier;

public interface Identifiable  {
    default String getNamespace() {
        return getIdentifier().getNamespace();
    }

    default String getPath() {
        return getIdentifier().getPath();
    }

    default Identifier getIdentifier() {
        return null;
    }
}
