package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.event.TimeStopManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            // 5秒停止
            TimeStopManager.start(serverLevel, player, 300);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 13));
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300, 200, false, false));
            // 10秒クールタイム
            player.getCooldowns().addCooldown(this, 400);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}