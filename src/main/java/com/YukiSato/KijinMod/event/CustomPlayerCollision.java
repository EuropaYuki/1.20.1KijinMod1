package com.YukiSato.KijinMod.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "kijinmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomPlayerCollision {
    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent event) {
        Entity entity = event.getEntity();
        // プレイヤーかどうかを確認し、プレイヤーの場合は当たり判定を無効にする
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getAbilities() != null) {
                if (player.getAbilities().flying) {
                    player.noPhysics = true;
                }
            }
        }

    }
}
