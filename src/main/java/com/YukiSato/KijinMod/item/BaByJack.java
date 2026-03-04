package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.NonFireLB;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Properties;

public class BaByJack extends Item {
    int saru;
    public BaByJack(Properties anyoe) {
        super(anyoe);
    }



    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            // 1. Nausea（視界ぐにゃぐにゃ）
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));

            // 2. 爆発！（でもプレイヤーは無傷）
            level.explode(null, player.getX(), player.getY(), player.getZ(), 10F, Level.ExplosionInteraction.NONE);

            // 3. メッセージ
            player.sendSystemMessage(Component.literal("You just ate Babyジャック...なぜ食べた！"));

            // 4. Sound（ばぶぅ♡）
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
