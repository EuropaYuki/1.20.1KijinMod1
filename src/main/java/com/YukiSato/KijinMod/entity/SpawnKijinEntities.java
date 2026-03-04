package com.YukiSato.KijinMod.entity;


import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "kijinmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnKijinEntities {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntitySpawn(LevelEvent event) {

    }

    private static void onDesertSpawns(LevelEvent event) {

    }
}
