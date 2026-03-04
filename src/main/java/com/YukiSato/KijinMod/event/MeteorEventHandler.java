package com.YukiSato.KijinMod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class MeteorEventHandler {
//    private static final Random RANDOM = new Random();
//
//    @SubscribeEvent
//    public static void onServerTick(TickEvent.ServerTickEvent event) {
//        if (event.phase == TickEvent.Phase.START) {
//            return;
//        }
//
//        // 20 ticks = 1 second
//        if (event.getServer().getTickCount() % 200 == 0) {
//            for (ServerLevel serverWorld : event.getServer().getAllLevels()) {
//                spawnMeteor(serverWorld);
//            }
//        }
//    }
//
//    private static void spawnMeteor(ServerLevel world) {
//        int x = RANDOM.nextInt(30000) - 15000;
//        int z = RANDOM.nextInt(30000) - 15000;
//        int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
//
//        BlockPos pos = new BlockPos(x, y, z);
//        world.explode(null, x, y, z, 3.0F, Level.ExplosionInteraction.NONE);
//    }
}
