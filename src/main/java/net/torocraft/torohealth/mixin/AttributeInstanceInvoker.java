package net.torocraft.torohealth.mixin;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;

@Mixin(EntityAttributeInstance.class)
public interface AttributeInstanceInvoker {

	@Invoker("getModifiersByOperation")
	public Collection<EntityAttributeModifier> invokeGetModifiersByOperation(Operation operation);

}
