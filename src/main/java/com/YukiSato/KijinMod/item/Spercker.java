package com.YukiSato.KijinMod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class Spercker extends Item {
    public Spercker() {
        super(new Properties().durability(6000).fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {

            // ここで周囲のエンティティにダメージを与えるロジックを追加します
            // 例: 範囲内のすべてのエンティティに5のダメージを与える
            double range = 10.0;
            List<Entity> entities = world.getEntities(player, player.getBoundingBox().inflate(range));
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).hurt(entity.damageSources().magic(), 5.0F);
                }
            }
        }
        return InteractionResultHolder.success(stack);
    }



}
