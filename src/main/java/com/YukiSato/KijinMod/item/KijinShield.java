package com.YukiSato.KijinMod.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.Vec3;

public class KijinShield extends ShieldItem {
    public KijinShield() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC).durability(5000));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity living, LivingEntity attacker) {
        Vec3 vec3 = attacker.getLookAngle();
        living.setDeltaMovement(attacker.getX() + 10 + vec3.x, attacker.getY() + 10 + vec3.y, attacker.getZ() + 10 + vec3.z);
        return super.hurtEnemy(stack, living, attacker);
    }
}
