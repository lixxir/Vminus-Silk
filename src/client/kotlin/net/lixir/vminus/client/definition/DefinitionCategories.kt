package net.lixir.vminus.client.definition

/**
 * Built-in implementation of [DefinitionCategory]
 *
 * @property id Stable string identifier for matching and identification
 * @see DefinitionCategory
 */
enum class DefinitionCategories(private val id: String) : DefinitionCategory {
    RENDER("render"),
    DATAGEN("datagen");

    override fun asString(): String = id

    override fun toString(): String = "DefinitionCategory[$id]"
}
