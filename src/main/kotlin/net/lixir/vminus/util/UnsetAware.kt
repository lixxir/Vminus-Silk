package net.lixir.vminus.util

/**
 * Marker interface for objects that have an "unset" or "empty" state.
 *
 * Implementations should provide ways to check whether the value has been
 * explicitly set or is currently considered empty/uninitialized.
 */
interface UnsetAware {
    val isEmpty: Boolean

    val isUnset: Boolean
}
