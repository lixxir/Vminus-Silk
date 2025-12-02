package net.lixir.vminus.client.definition

import net.minecraft.util.StringIdentifiable

/**
 * Represents a type of definition category.
 *
 * Implementations (usually enums) define categories for definitions to be used by the client during datagen, setup, and other processes.
 *
 * @see [DefinitionCategories]
 * @see [AbstractDefinitionRegistry]
 */
interface DefinitionCategory : StringIdentifiable
