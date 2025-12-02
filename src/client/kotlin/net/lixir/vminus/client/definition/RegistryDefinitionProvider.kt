package net.lixir.vminus.client.definition

interface RegistryDefinitionProvider<T : AbstractDefinitionRegistry<*>> {
    fun run()
}
