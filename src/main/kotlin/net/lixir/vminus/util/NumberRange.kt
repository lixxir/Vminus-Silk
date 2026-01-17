package net.lixir.vminus.util

data class NumberRange<T>(val min: T, val max: T) where T : Number, T : Comparable<T> {
    fun contains(value: T) = value in min..max

    override fun toString() = "Range[$min, $max]"

    init {
        require(min <= max) { "Min cannot be greater than max" }
    }
}
