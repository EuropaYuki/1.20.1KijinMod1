package com.YukiSato.KijinMod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;


public class InvisibilityEffect extends MobEffect {
    public InvisibilityEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xffffff);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_19468_) {
        entity.setInvisible(true);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;  // エフェクトを毎ティック適用
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }
}
