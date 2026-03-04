package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.item.ExtraRarity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class Hinokami extends SwordItem {
    public Hinokami() {
        super(KijinModTiers.SAKURITE, 4, -2.4F, new Properties().rarity(ExtraRarity.ULTIMATE).fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Vec3 vec3 = attacker.getLookAngle();
        target.setSecondsOnFire(10);
        target.randomTeleport(attacker.getX() + vec3.x * 3, attacker.getY() + 5, attacker.getZ() + vec3.z * 3, true);
        double range = 60.0; // 範囲攻撃の半径
        List<LivingEntity> nearbyEntities = attacker.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(range));
        for (LivingEntity entity : nearbyEntities) {
            if (entity != attacker && entity != target) {
                if (entity.getType() != MobEntities.NINJA.get() && entity.getType() != MobEntities.CAMERAMAN.get() && entity.getType() != EntityType.CREEPER && entity.getClassification(true) == MobCategory.MONSTER) {
                    entity.setSecondsOnFire(10); // 範囲内の敵にも炎ダメージを与える
                    entity.randomTeleport(attacker.getX() + vec3.x * 3, attacker.getY() + 5, attacker.getZ() + vec3.z * 3, true);
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }


}
