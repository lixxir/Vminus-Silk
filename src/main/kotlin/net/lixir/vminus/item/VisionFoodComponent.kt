package net.lixir.vminus.item

import com.mojang.datafixers.util.Pair
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.sound.SoundEvent

class VisionFoodComponent(
    val nutrition: Int?, val saturation: Float?, val alwaysEdible: Boolean?,
    val eatSound: SoundEvent?, val burpSound: SoundEvent?,
    val effects: List<Pair<StatusEffectInstance?, Float?>>?
) {
    fun merge(vanilla: FoodComponent?): FoodComponent {
        val builder = getBuilder(vanilla)
        if (this.effects != null)
            this.effects.forEach { pair: Pair<StatusEffectInstance?, Float?> -> builder.statusEffect(pair.first, pair.second!!) }
        else
            vanilla?.effects()?.forEach { entry: FoodComponent.StatusEffectEntry? ->
                if (entry != null)
                    builder.statusEffect(entry.effect, entry.probability)
            }

        if (this.alwaysEdible == true || (vanilla != null && vanilla.canAlwaysEat())) {
            builder.alwaysEdible()
        }

        return builder.build()
    }

    private fun getBuilder(vanilla: FoodComponent?): FoodComponent.Builder {
        val builder = FoodComponent.Builder()

        val nutrition = this.nutrition ?: (vanilla?.nutrition()
            ?: 0)
        builder.nutrition(nutrition)

        val saturation = this.saturation ?: (vanilla?.saturation()
            ?: 0.0f)
        builder.saturationModifier(saturation)
        return builder
    }
}
