package net.torocraft.torohealth.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;

public class UnlimitedValue {
    
    public static double getWideRangedAttribute (LivingEntity entity, EntityAttribute attribute) {
        AttributeContainer entityAttributeContainer = entity.getAttributes();
        EntityAttributeInstance entityAttributeInstance = entityAttributeContainer.getCustomInstance(attribute);
        if (entityAttributeInstance != null) {
            double d = entityAttributeInstance.getBaseValue();
            for (EntityAttributeModifier entityAttributeModifier : entityAttributeInstance.getModifiers(Operation.ADDITION)) {
                d += entityAttributeModifier.getValue();
            }
            double d2 = d;
            for (EntityAttributeModifier entityAttributeModifier : entityAttributeInstance.getModifiers(Operation.MULTIPLY_BASE)) {
                d2 += d * entityAttributeModifier.getValue();
            }
            for (EntityAttributeModifier entityAttributeModifier : entityAttributeInstance.getModifiers(Operation.MULTIPLY_TOTAL)) {
                d2 *= 1.0 + entityAttributeModifier.getValue();
            }
            return d2;
        } else {
            return entityAttributeContainer.getBaseValue(attribute);
        }
    }
    
}
