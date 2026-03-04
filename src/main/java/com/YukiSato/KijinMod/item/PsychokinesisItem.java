package com.YukiSato.KijinMod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class PsychokinesisItem extends Item {
    public PsychokinesisItem() {
        super(new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            // 効果の実行
            performPsychokinesis(level, player);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }
    private void performPsychokinesis(Level world, Player player) {
        List<Entity> nearbyEntities = world.getEntities(player, player.getBoundingBox().inflate(100.0));
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) {
                entity.push(0, 2, 0);
                entity.fallDistance = 0;      // 落下ダメージをリセット
            }
        }
    }
}
