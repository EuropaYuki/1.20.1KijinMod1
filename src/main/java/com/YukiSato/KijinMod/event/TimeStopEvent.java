package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.main.KijinMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KijinMod.MOD_ID)
public class TimeStopEvent {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();

        if (living.level().isClientSide) return;
        if (!(living.level() instanceof ServerLevel serverLevel)) return;
        if (!TimeStopManager.isStopped(serverLevel)) return;

        // 発動者本人は止めない
        if (living instanceof Player player && TimeStopManager.isOwner(serverLevel, player.getUUID())) {
            return;
        }

        // 移動停止
        living.setDeltaMovement(Vec3.ZERO);
        living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 200, false, false));

        // 位置を戻して実質停止
        living.setPos(living.xo, living.yo, living.zo);
        living.hurtMarked = true;

        if (living instanceof Mob mob) {
            mob.getNavigation().stop();
            mob.setTarget(null);
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel serverLevel)) return;
        if (!TimeStopManager.isStopped(serverLevel)) return;

        for (Entity entity : serverLevel.getAllEntities()) {

            // LivingEntity は上で止める
            if (entity instanceof LivingEntity) continue;

            // 発動者本人は除外
            if (entity instanceof Player player && TimeStopManager.isOwner(serverLevel, player.getUUID())) {
                continue;
            }

            // 飛び道具と落ちアイテムを停止
            if (entity instanceof Projectile || entity instanceof 
                    ItemEntity) {
                entity.setDeltaMovement(Vec3.ZERO);
                entity.setPos(entity.xo, entity.yo, entity.zo);
                entity.hurtMarked = true;
            }
        }
    }
}
