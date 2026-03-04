package com.YukiSato.KijinMod.event;


import com.YukiSato.KijinMod.entity.NonFireLB;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class LBEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase == TickEvent.Phase.END) {
//            Player player = event.player;
//            Level world = player.level();
//            UseOnContext context = new UseOnContext(player, player.getUsedItemHand(), null);
//            Level level = context.getLevel();
//
//            if (!world.isClientSide) {
//                List<Monster> mobs = world.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(8));
//                for (Mob mob : mobs) {
//                    if (mob.isAlive() && mob.distanceTo(player) <= 8) {
//                        BlockPos pos = mob.blockPosition();
//                        LightningBolt nonFire = new NonFireLB(level);
//                        nonFire.moveTo(Vec3.atBottomCenterOf(pos));
//                        nonFire.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
//                        level.addFreshEntity(nonFire);
//                    }
//                }
//            }
//        }
    }
}
