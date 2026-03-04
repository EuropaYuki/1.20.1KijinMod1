package com.YukiSato.KijinMod.effect;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class FreezeEffect extends MobEffect {
    public FreezeEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFFFFF);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int p_19468_) {
        entity.setDeltaMovement(0, 0, 0); // 移動速度を0に設定
        entity.setNoGravity(true);  // 重力の影響を受けないように設定
        ((PathfinderMob)entity).getNavigation().stop();
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
