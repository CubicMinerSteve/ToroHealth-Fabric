package net.torocraft.torohealth.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;

public class ValueUtils {

	public static double bypassLimit(LivingEntity entity, EntityAttribute attribute) {
		// Get the instance containing all types of attributes.
		AttributeContainer attrContainer = entity.getAttributes();

		// Get a specific type of attribute.
		EntityAttributeInstance attrInstance = attrContainer.getCustomInstance(attribute);

		// If not null then calculate the final value bypassing the upper limit.
		if (attrInstance != null) {
			// Base value of the attribute.
			double d = attrInstance.getBaseValue();

			// Store the value for backup. Safer! :)
			double e = d;

			// Get a collection of the modifiers and then iterate over all according to the
			// operation type.
			for (EntityAttributeModifier currentModifier : attrInstance.getModifiers(Operation.ADDITION)) {
				e += currentModifier.getValue();
			}

			// Same above but a different type.
			for (EntityAttributeModifier currentModifier : attrInstance.getModifiers(Operation.MULTIPLY_BASE)) {
				e += d * currentModifier.getValue();
			}

			// Same above.
			for (EntityAttributeModifier currentModifier : attrInstance.getModifiers(Operation.MULTIPLY_TOTAL)) {
				e *= 1.0 + currentModifier.getValue();
			}
			return e;
		} else {
			return attrContainer.getBaseValue(attribute);
		}
	}

}
