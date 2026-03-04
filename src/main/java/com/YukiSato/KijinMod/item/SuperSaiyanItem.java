package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SuperSaiyanItem extends Item {
    public SuperSaiyanItem() {
        super(new Properties().stacksTo(1).fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == KijinModItems.SUPER_SAIYAN_ITEM.get()) {
//            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 20 * 60 * 30, 4));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 10, 25));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 10, 3));
        }
//        if (player.hasEffect(MobEffects.DAMAGE_BOOST) == true && player.hasEffect(MobEffects.JUMP) == true) {
//            player.getCooldowns().addCooldown(this, 10000);
//        }
        return InteractionResultHolder.consume(stack);
    }
}
