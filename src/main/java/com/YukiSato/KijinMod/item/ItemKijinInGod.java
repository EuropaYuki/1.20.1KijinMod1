package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.regi.KijinModTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class ItemKijinInGod extends Item {
    public ItemKijinInGod() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(75));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(KijinModTags.Items.KIJIN_SERIES)) {
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 60 * 3, 1));
        } else {
            player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * 60 * 3, 1));
        }
        return InteractionResultHolder.consume(stack);
    }
}
