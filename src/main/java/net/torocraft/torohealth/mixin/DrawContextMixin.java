package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(DrawContext.class)
public interface DrawContextMixin {

   @Accessor("matrices")
   public void setMatrices(MatrixStack matrixStack);

}
