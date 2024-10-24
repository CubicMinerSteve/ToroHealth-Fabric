package net.torocraft.torohealth.util;

import java.util.Collection;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.registry.entry.RegistryEntry;
import net.torocraft.torohealth.mixin.AttributeInstanceInvoker;

public class ValueUtils {

	public static double bypassLimit(LivingEntity entity, RegistryEntry<EntityAttribute> attribute) {
		// Get the instance containing all types of attributes.
		AttributeContainer entityAttributeContainer = entity.getAttributes();

		// Get a specific type of attribute.
		EntityAttributeInstance entityAttributeInstance = entityAttributeContainer.getCustomInstance(attribute);

		// If not null then calculate the final value bypassing the upper limit.
		if (entityAttributeInstance != null) {
			// Base value of the attribute.
			double d = entityAttributeInstance.getBaseValue();

			// Get a collection of the modifiers and then iterate over all according to the
			// operation type.
			Collection<EntityAttributeModifier> allModifiers = ((AttributeInstanceInvoker) entityAttributeInstance)
					.invokeGetModifiersByOperation(Operation.ADD_VALUE);
			for (EntityAttributeModifier current : allModifiers) {
				d += current.value();
			}

			// Store the value for backup. Safer! :)
			double e = d;

			// Same above but a different type.
			allModifiers = ((AttributeInstanceInvoker) entityAttributeInstance)
					.invokeGetModifiersByOperation(Operation.ADD_MULTIPLIED_BASE);
			for (EntityAttributeModifier current : allModifiers) {
				e += d * current.value();
			}
			allModifiers = ((AttributeInstanceInvoker) entityAttributeInstance)
					.invokeGetModifiersByOperation(Operation.ADD_MULTIPLIED_TOTAL);
			for (EntityAttributeModifier current : allModifiers) {
				e *= 1.0 + current.value();
			}
			return e;
		} else {
			return entityAttributeContainer.getBaseValue(attribute);
		}
	}

}
