package com.YukiSato.KijinMod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Scouter extends Item {
    public Scouter() {
        super(new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player != null) {
            Vec3 motion = player.getDeltaMovement();
            double speed = 10; // 移動速度を調整する必要があります
            Vec3 newMotion = new Vec3(motion.x() * speed, motion.y(), motion.z() * speed);
            player.setDeltaMovement(newMotion);
        }
        AABB area = new AABB(player.getX() + 50, player.getY() + 50, player.getZ() + 50,
                player.getX() - 50, player.getY() - 50, player.getZ() - 50);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            if (entity.getClassification(true) == MobCategory.MONSTER) {
                player.setHealth(player.getHealth() + entity.getHealth());
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 6000000, 0));
                player.level().explode(entity, entity.getX(), entity.getY() - 1, entity.getZ(), 10F, true, Level.ExplosionInteraction.NONE);
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

}
