package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.AbstractDefinition
import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories
import net.lixir.vminus.client.datagen.lang.LangKey

abstract class DataGenDefinition<D : DataGenDefinition<D, T>, T> protected constructor() : AbstractDefinition<D, T>() {
    var langKey: LangKey = LangKey.UNSET
        protected set

    override val type: DefinitionCategory
        get() = DefinitionCategories.DATAGEN

    fun langKey(langKey: LangKey): D {
        this.langKey = langKey
        return this as D
    }

    fun langKey(lang: String): D {
        return langKey(LangKey.of(lang))
    }

    @Suppress("UNCHECKED_CAST")
    override fun merge(other: D?): D {
        if (other == null) return this as D
        langKey = if (langKey.isUnset) other.langKey else langKey
        return super.merge(other)
    }
}
