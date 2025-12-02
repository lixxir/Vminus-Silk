package net.lixir.vminus.extension

object ArrayExtension {
    inline fun <reified T> Array<*>.getByType(value: Any): T? {
        return filterIsInstance<T>().firstOrNull { it == value }
    }

}