package net.lixir.vminus.component

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import org.ladysnake.cca.api.v3.entity.RespawnableComponent

class CapeComponentImpl(private val player: PlayerEntity) : CapeComponent, RespawnableComponent<CapeComponentImpl?> {
    private var selectedName: String = ""


    override fun readFromNbt(nbtCompound: NbtCompound, wrapperLookup: WrapperLookup) {
        selectedName = nbtCompound.getString("Cape")
    }

    override fun writeToNbt(nbtCompound: NbtCompound, wrapperLookup: WrapperLookup) {
        nbtCompound.putString("Cape", selectedName)
    }

    override var capeName: String
        get() = selectedName
        set(name) {
            if (this.selectedName != name) {
                this.selectedName = name
                VminusComponents.CAPE.sync(this.player)
            }
        }
}
