package com.YukiSato.KijinMod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TimeStopItem extends Item {
    public TimeStopItem() {
        super(new Properties().rarity(ExtraRarity.ULTIMATE).fireResistant().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            // プレイヤーの周囲10ブロックの範囲を定義
            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(
                    world.getWorldBorder().getMinX(), world.getMinBuildHeight(), world.getWorldBorder().getMinZ(),
                    world.getWorldBorder().getMaxX(), world.getMaxBuildHeight(), world.getWorldBorder().getMaxZ()));

            for (LivingEntity entity : entities) {
                if (entity instanceof Mob) {
                    ((Mob) entity).setNoAi(true);
                    ((Mob) entity).getNavigation().stop();
                }
            }
            for (LivingEntity entity : entities) {
                Mob mob = (Mob) entity;
                if (entity instanceof Mob) {
                    // モブのナビゲーションを停止し、動けなくする
                    mob.getNavigation().stop();
                    mob.setNoAi(true);  // AIを停止
                }
            }
            player.getItemInHand(hand).hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));  // アイテムの耐久値を消耗
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}