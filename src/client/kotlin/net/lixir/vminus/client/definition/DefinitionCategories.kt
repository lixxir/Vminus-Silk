package net.lixir.vminus.client.definition

import net.lixir.vminus.Vminus
import net.minecraft.util.Identifier

/**
 * Built-in implementation of [DefinitionCategory]
 *
 * @property id Stable string identifier for matching and identification
 * @see DefinitionCategory
 */
enum class DefinitionCategories(val id: Identifier) : DefinitionCategory {
    RENDER(Vminus.id("render")),
    DATAGEN(Vminus.id("datagen"));

    override fun getIdentifier() = id

    override fun toString() = "DefinitionCategory{$id}"
}
