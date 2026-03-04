package com.YukiSato.KijinMod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Timer;
import java.util.TimerTask;

public class Kaioken extends Item {
    int a = 0;
    public Kaioken() {
        super(new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player) entity;
        if (a == 1) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 100, 2));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 2));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    player.hurt(player.damageSources().freeze(), 1F);
                }
            }, 1000);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        a = 1;
        player.startUsingItem(hand);
        if (player.isSteppingCarefully()) {
            a = 0;
            player.removeEffect(MobEffects.DAMAGE_BOOST);
            player.removeEffect(MobEffects.MOVEMENT_SPEED);
            player.removeEffect(MobEffects.JUMP);
            player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        }
        return InteractionResultHolder.success(stack);
    }
}
