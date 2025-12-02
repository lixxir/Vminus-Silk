package net.lixir.vminus.vision.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record VisionEntityVariant(@Nullable Identifier name, @Nullable Identifier texture, Integer weight,
                                  Boolean replace) {
}
