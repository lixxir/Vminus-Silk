package net.lixir.vminus.client.datagen.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Represents a language translation key in VMinus.
 * <p>
 * Encapsulates a translation key string and provides utility methods
 * for checking if a key is empty or unset. Ensures consistent behavior
 * for equality, hash code, and string representation.
 * <p>
 * Predefined constants:
 * <ul>
 *   <li>{@link #UNSET} – indicates a key has not been assigned and will be set to the default translation.</li>
 *   <li>{@link #NONE} – represents an intentionally empty key.</li>
 * </ul>
 */

public final class Translation {
    private final @NotNull String key;
    private Translation(@NotNull String key) {
        this.key = key;
    }
    public static final @NotNull Translation UNSET = Translation.of("unset");

    /**
     * Creates a new {@link Translation} from a string.
     * <p>
     * If the string is blank, the unset key is returned.
     *
     * @param key the raw key string
     * @return a new {@link Translation} instance
     */
    public static @NotNull Translation of(@NotNull String key) {
        return key.isBlank() ? UNSET : new Translation(key);
    }    public static final @NotNull Translation NONE = Translation.of("none");

    /**
     * Checks if this key is default.
     *
     * @return true if the key is {@link #UNSET}
     */
    public boolean isUnset() {
        return this.equals(UNSET);
    }

    /**
     * Checks if this key is empty or default.
     *
     * @return true if the key is {@link #NONE} or empty.
     */
    public boolean isEmpty() {
        return this.equals(NONE) || key.isEmpty();
    }

    /**
     * Returns the raw key string.
     *
     * @return the key string
     */
    public @NotNull String getKey() {
        return key;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Translation other))
            return false;
        return key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "LangKey{" + key + "}";
    }




}
