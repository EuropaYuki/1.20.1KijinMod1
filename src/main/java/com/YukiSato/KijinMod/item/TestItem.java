package com.YukiSato.KijinMod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class TestItem extends Item {

    public TestItem() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int count = 0;
        int input = 18;
        for (int n = 2; n < input; n++) {
            if (input % n == 0) {
                player.sendSystemMessage(Component.literal("素数じゃない"));
                return InteractionResultHolder.consume(stack);
            } else {
                count++;
            }
        }
        if (count == input - 2) {
            player.sendSystemMessage(Component.literal("素数だ"));
        }
        return InteractionResultHolder.consume(stack);
    }
}
