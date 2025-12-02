package net.lixir.vminus

import net.lixir.vminus.vision.Vision.Companion.getValue
import net.lixir.vminus.vision.Vision.Companion.getValues
import net.lixir.vminus.vision.property.EntityTypeVisionProperties
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.world.entity.EntityLike

object VminusHooks {
    @JvmStatic
    fun onEntityAdded(entityLike: EntityLike): Boolean {
        if (entityLike is LivingEntity) {
            val baseAttributeValues =
                getValues(entityLike, EntityTypeVisionProperties.BASE_ATTRIBUTE)
            for (baseAttribute in baseAttributeValues) {
                val attribute = baseAttribute.attribute
                val value = baseAttribute.value
                val attributeInstance = entityLike.getAttributeInstance(attribute)
                if (attributeInstance != null) attributeInstance.baseValue = value
            }
        }
        if (entityLike is Entity) {
            val ban = getValue(entityLike, EntityTypeVisionProperties.BAN)
            return java.lang.Boolean.TRUE == ban
        }
        return false
    }
}
