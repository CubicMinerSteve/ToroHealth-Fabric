package net.torocraft.torohealth.hud;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.ColorFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import me.shedaniel.math.Color;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.AnchorPoint;
import net.torocraft.torohealth.config.Config.Mode;
import net.torocraft.torohealth.config.Config.NumberType;

public class HudConfigScreen {

	public static Screen buildConfigScreen(Screen parentScreen) {

		ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parentScreen)
				.setTitle(Text.translatable("torohealth.main.title")).setSavingRunnable(() -> {
					ToroHealth.CONFIG_LOADER.save(ToroHealth.CONFIG);
				});
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		ConfigCategory catGeneric = builder.getOrCreateCategory(Text.translatable("torohealth.generic.title"));

		BooleanToggleBuilder toggleEnabled = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.generic.enabled"), ToroHealth.CONFIG.enabled)
				.setDefaultValue(true).setSaveConsumer(enabled -> ToroHealth.CONFIG.enabled = enabled);
		BooleanToggleBuilder toggleWatchForChanges = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.generic.watchForChanges"), ToroHealth.CONFIG.watchForChanges)
				.setDefaultValue(true).setSaveConsumer(watchForChanges -> ToroHealth.CONFIG.watchForChanges = watchForChanges);

		SubCategoryBuilder subCatHudBuilder = entryBuilder.startSubCategory(Text.translatable("torohealth.hud.title"));
		IntSliderBuilder slideHudDistance = entryBuilder
				.startIntSlider(Text.translatable("torohealth.hud.distance"), (int) ToroHealth.CONFIG.hud.distance, 0, 96)
				.setDefaultValue(60).setSaveConsumer(hudDistance -> ToroHealth.CONFIG.hud.distance = hudDistance);
		IntSliderBuilder sliderHudX = entryBuilder
				.startIntSlider(Text.translatable("torohealth.hud.x"), (int) ToroHealth.CONFIG.hud.x, -80, 80)
				.setDefaultValue(4).setSaveConsumer(x -> ToroHealth.CONFIG.hud.x = x);
		IntSliderBuilder sliderHudY = entryBuilder
				.startIntSlider(Text.translatable("torohealth.hud.y"), (int) ToroHealth.CONFIG.hud.y, -80, 80)
				.setDefaultValue(4).setSaveConsumer(y -> ToroHealth.CONFIG.hud.y = y);
		IntSliderBuilder sliderHudScale = entryBuilder
				.startIntSlider(Text.translatable("torohealth.hud.scale"), (int) (ToroHealth.CONFIG.hud.scale * 100), 0, 100)
				.setDefaultValue(100).setSaveConsumer(scale -> ToroHealth.CONFIG.hud.scale = scale * 0.01F);
		IntSliderBuilder sliderHideDelay = entryBuilder
				.startIntSlider(Text.translatable("torohealth.hud.hideDelay"), (int) ToroHealth.CONFIG.hud.hideDelay, 0, 40)
				.setDefaultValue(20).setSaveConsumer(hideDelay -> ToroHealth.CONFIG.hud.hideDelay = hideDelay);
		EnumSelectorBuilder<AnchorPoint> enumAnchorPoint = entryBuilder
				.startEnumSelector(Text.translatable("torohealth.hud.anchorPoint"), Config.AnchorPoint.class, ToroHealth.CONFIG.hud.anchorPoint)
				.setEnumNameProvider(point -> {
					if (point.equals(AnchorPoint.TOP_LEFT)) {
						return Text.translatable("torohealth.hud.anchorPoint.topLeft");
					} else if (point.equals(AnchorPoint.TOP_CENTER)) {
						return Text.translatable("torohealth.hud.anchorPoint.topCenter");
					} else if (point.equals(AnchorPoint.TOP_RIGHT)) {
						return Text.translatable("torohealth.hud.anchorPoint.topRight");
					} else if (point.equals(AnchorPoint.BOTTOM_LEFT)) {
						return Text.translatable("torohealth.hud.anchorPoint.bottomLeft");
					} else if (point.equals(AnchorPoint.BOTTOM_CENTER)) {
						return Text.translatable("torohealth.hud.anchorPoint.bottomCenter");
					} else {
						return Text.translatable("torohealth.hud.anchorPoint.bottomRight");
					}
				}).setDefaultValue(AnchorPoint.TOP_LEFT)
				.setSaveConsumer(anchorPoint -> ToroHealth.CONFIG.hud.anchorPoint = anchorPoint);
		BooleanToggleBuilder toggleShowEntity = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.hud.showEntity"), ToroHealth.CONFIG.hud.showEntity)
				.setDefaultValue(true).setSaveConsumer(showEntity -> ToroHealth.CONFIG.hud.showEntity = showEntity);
		BooleanToggleBuilder toggleShowBar = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.hud.showBar"), ToroHealth.CONFIG.hud.showBar)
				.setDefaultValue(true).setSaveConsumer(showBar -> ToroHealth.CONFIG.hud.showBar = showBar);
		BooleanToggleBuilder toggleShowSkin = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.hud.showSkin"), ToroHealth.CONFIG.hud.showSkin)
				.setDefaultValue(true).setSaveConsumer(showSkin -> ToroHealth.CONFIG.hud.showSkin = showSkin);
		BooleanToggleBuilder toggleHudOnlyWhenHurt = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.hud.onlyWhenHurt"), ToroHealth.CONFIG.hud.onlyWhenHurt)
				.setDefaultValue(false).setSaveConsumer(onlyWhenHurt -> ToroHealth.CONFIG.hud.onlyWhenHurt = onlyWhenHurt);

		subCatHudBuilder.add(slideHudDistance.build());
		subCatHudBuilder.add(sliderHudX.build());
		subCatHudBuilder.add(sliderHudY.build());
		subCatHudBuilder.add(sliderHudScale.build());
		subCatHudBuilder.add(sliderHideDelay.build());
		subCatHudBuilder.add(enumAnchorPoint.build());
		subCatHudBuilder.add(toggleShowEntity.build());
		subCatHudBuilder.add(toggleShowBar.build());
		subCatHudBuilder.add(toggleShowSkin.build());
		subCatHudBuilder.add(toggleHudOnlyWhenHurt.build());

		SubCategoryBuilder subCatBarBuilder = entryBuilder.startSubCategory(Text.translatable("torohealth.bar.title"));
		EnumSelectorBuilder<NumberType> enumDamageNumberType = entryBuilder
				.startEnumSelector(Text.translatable("torohealth.bar.damageNumberType"), Config.NumberType.class, ToroHealth.CONFIG.bar.damageNumberType)
				.setEnumNameProvider(type -> {
					if (type.equals(NumberType.NONE)) {
						return Text.translatable("torohealth.bar.damageNumberType.none");
					} else if (type.equals(NumberType.CUMULATIVE)) {
						return Text.translatable("torohealth.bar.damageNumberType.cumulative");
					} else {
						return Text.translatable("torohealth.bar.damageNumberType.last");
					}
				}).setDefaultValue(NumberType.LAST).setSaveConsumer(damageNumberType -> ToroHealth.CONFIG.bar.damageNumberType = damageNumberType);
		ColorFieldBuilder friendColorBuilder = entryBuilder
				.startColorField(Text.translatable("torohealth.bar.friendColor"), Color.ofTransparent(ToroHealth.CONFIG.bar.friendColor))
				.setDefaultValue(0x00ff00).setSaveConsumer(friendColor -> ToroHealth.CONFIG.bar.friendColor = friendColor);
		ColorFieldBuilder friendSecondaryColorBuilder = entryBuilder
				.startColorField(Text.translatable("torohealth.bar.friendColorSecondary"), Color.ofTransparent(ToroHealth.CONFIG.bar.friendColorSecondary))
				.setDefaultValue(0x008000).setSaveConsumer(friendColorSecondary -> ToroHealth.CONFIG.bar.friendColorSecondary = friendColorSecondary);
		ColorFieldBuilder foeColorBuilder = entryBuilder
				.startColorField(Text.translatable("torohealth.bar.foeColor"), Color.ofTransparent(ToroHealth.CONFIG.bar.foeColor))
				.setDefaultValue(0xff0000).setSaveConsumer(foeColor -> ToroHealth.CONFIG.bar.foeColor = foeColor);
		ColorFieldBuilder foeSecondaryColorBuilder = entryBuilder
				.startColorField(Text.translatable("torohealth.bar.foeColorSecondary"), Color.ofTransparent(ToroHealth.CONFIG.bar.foeColorSecondary))
				.setDefaultValue(0x800000).setSaveConsumer(foeColorSecondary -> ToroHealth.CONFIG.bar.foeColorSecondary = foeColorSecondary);

		subCatBarBuilder.add(enumDamageNumberType.build());
		subCatBarBuilder.add(friendColorBuilder.build());
		subCatBarBuilder.add(friendSecondaryColorBuilder.build());
		subCatBarBuilder.add(foeColorBuilder.build());
		subCatBarBuilder.add(foeSecondaryColorBuilder.build());

		SubCategoryBuilder subInWorldBuilder = entryBuilder.startSubCategory(Text.translatable("torohealth.inWorld.title"));
		EnumSelectorBuilder<Mode> enumInWorldMode = entryBuilder
				.startEnumSelector(Text.translatable("torohealth.inWorld.mode"), Config.Mode.class, ToroHealth.CONFIG.inWorld.mode)
				.setEnumNameProvider(mode -> {
					if (mode.equals(Mode.NONE)) {
						return Text.translatable("torohealth.inWorld.mode.none");
					} else if (mode.equals(Mode.WHEN_HOLDING_WEAPON)) {
						return Text.translatable("torohealth.inWorld.mode.whenHoldingWeapon");
					} else {
						return Text.translatable("torohealth.inWorld.mode.always");
					}
				}).setDefaultValue(Mode.ALWAYS).setSaveConsumer(mode -> ToroHealth.CONFIG.inWorld.mode = mode);
		IntSliderBuilder sliderInWorldDistance = entryBuilder
				.startIntSlider(Text.translatable("torohealth.inWorld.distance"), (int) ToroHealth.CONFIG.inWorld.distance, 0, 96)
				.setDefaultValue(60).setSaveConsumer(inWorldDistance -> ToroHealth.CONFIG.inWorld.distance = inWorldDistance);
		BooleanToggleBuilder toggleOnlyWhenLookingAt = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.inWorld.onlyWhenLookingAt"), ToroHealth.CONFIG.inWorld.onlyWhenLookingAt)
				.setDefaultValue(false).setSaveConsumer(onlyWhenLookingAt -> ToroHealth.CONFIG.inWorld.onlyWhenLookingAt = onlyWhenLookingAt);
		BooleanToggleBuilder toggleInWorldOnlyWhenHurt = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.inWorld.onlyWhenHurt"), ToroHealth.CONFIG.inWorld.onlyWhenHurt)
				.setDefaultValue(false).setSaveConsumer(inWorldOnlyWhenHurt -> ToroHealth.CONFIG.inWorld.onlyWhenHurt = inWorldOnlyWhenHurt);

		subInWorldBuilder.add(enumInWorldMode.build());
		subInWorldBuilder.add(sliderInWorldDistance.build());
		subInWorldBuilder.add(toggleOnlyWhenLookingAt.build());
		subInWorldBuilder.add(toggleInWorldOnlyWhenHurt.build());

		SubCategoryBuilder subParticleBuilder = entryBuilder.startSubCategory(Text.translatable("torohealth.particle.title"));
		BooleanToggleBuilder toggleParticleShow = entryBuilder
				.startBooleanToggle(Text.translatable("torohealth.particle.show"), ToroHealth.CONFIG.particle.show)
				.setDefaultValue(true).setSaveConsumer(particleShow -> ToroHealth.CONFIG.particle.show = particleShow);
		IntSliderBuilder sliderParticleDistance = entryBuilder
				.startIntSlider(Text.translatable("torohealth.particle.distance"), (int) ToroHealth.CONFIG.inWorld.distance, 0, 96)
				.setDefaultValue(60).setSaveConsumer(particleDistance -> ToroHealth.CONFIG.particle.distance = particleDistance);
		ColorFieldBuilder damageColorBuilder = entryBuilder
				.startColorField(Text.translatable("torohealth.particle.damageColor"), Color.ofTransparent(ToroHealth.CONFIG.particle.damageColor))
				.setDefaultValue(0xff0000).setSaveConsumer(damageColor -> ToroHealth.CONFIG.particle.damageColor = damageColor);
		ColorFieldBuilder healColorBuilder = entryBuilder
				.startColorField(Text.translatable("torohealth.particle.healColor"), Color.ofTransparent(ToroHealth.CONFIG.particle.healColor))
				.setDefaultValue(0x00ff00).setSaveConsumer(healColor -> ToroHealth.CONFIG.particle.healColor = healColor);
		
		subParticleBuilder.add(toggleParticleShow.build());
		subParticleBuilder.add(sliderParticleDistance.build());
		subParticleBuilder.add(damageColorBuilder.build());
		subParticleBuilder.add(healColorBuilder.build());

		catGeneric.addEntry(toggleEnabled.build());
		catGeneric.addEntry(toggleWatchForChanges.build());
		catGeneric.addEntry(subCatHudBuilder.build());
		catGeneric.addEntry(subCatBarBuilder.build());
		catGeneric.addEntry(subInWorldBuilder.build());
		catGeneric.addEntry(subParticleBuilder.build());

		return builder.build();
	}
}
