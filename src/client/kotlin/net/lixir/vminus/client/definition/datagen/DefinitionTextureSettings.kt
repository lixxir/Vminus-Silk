package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.Definition.Companion.IDENTIFIER_UNSET
import net.lixir.vminus.client.definition.Definition.Companion.STRING_UNSET
import net.minecraft.util.Identifier

class DefinitionTextureSettings {

    var addSuffix: String = STRING_UNSET
    var removeSuffix: String = STRING_UNSET
    var override: Identifier = IDENTIFIER_UNSET

    fun merge(other: DefinitionTextureSettings?): DefinitionTextureSettings {
        if (other == null)
            return this
        this.addSuffix = if (this.addSuffix == STRING_UNSET) other.addSuffix else this.addSuffix
        this.removeSuffix = if (this.removeSuffix == STRING_UNSET) other.removeSuffix else this.removeSuffix
        this.override = if (this.override == IDENTIFIER_UNSET) other.override else this.override
        return this
    }

    val empty: Boolean
        get() = this == EMPTY

    companion object {
        val EMPTY: DefinitionTextureSettings = DefinitionTextureSettings()
    }
}
