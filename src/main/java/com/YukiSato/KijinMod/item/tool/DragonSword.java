package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.item.ExtraRarity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DragonSword extends SwordItem {
    public DragonSword() {
        super(KijinModTiers.SAKURITE, 20, -3.0F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Vec3 vec3 = player.getLookAngle();
        player.playSound(SoundEvents.ENDER_DRAGON_GROWL, 1.0F, 1.0F);
        // ドラゴンソードを振ると大爆発が発生
        if (!world.isClientSide) {
            BlockPos pos = player.getOnPos();
            world.explode(player, vec3.x * 2, vec3.y * 2, vec3.z * 2, 10.0F, Level.ExplosionInteraction.MOB);
        }
        return InteractionResultHolder.success(itemStack);
    }

}
