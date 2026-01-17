package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.Definition
import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories
import net.lixir.vminus.client.datagen.lang.Translation

abstract class DatagenDefinition<T> : Definition<T>() {
    var translation = Translation.UNSET
        protected set

    override val category: DefinitionCategory
        get() = DefinitionCategories.DATAGEN

    open fun translation(translation: Translation): DatagenDefinition<T> {
        this.translation = translation
        return this
    }

    open fun translation(lang: String): DatagenDefinition<T> {
        return translation(Translation.of(lang))
    }

    override fun merge(other: Definition<T>?): Definition<T> {
        if (other == null || other !is DatagenDefinition<T>)
            return this
        translation = if (translation.isUnset) other.translation else translation
        return super.merge(other)
    }
}
