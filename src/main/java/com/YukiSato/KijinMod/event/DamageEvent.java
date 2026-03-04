package com.YukiSato.KijinMod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "kijinmod", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DamageEvent {

    @SubscribeEvent
    public static void hu (LivingHurtEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            LivingEntity entity = event.getEntity();
            Float source = event.getAmount();
            if (entity.getType() != EntityType.PLAYER) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 40, 3, false, false));
                player.displayClientMessage(Component.literal("Damage:" + source), true);
            }

        }
    }
}
