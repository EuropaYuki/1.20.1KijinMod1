package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.entity.custom.CameramanEntity;
import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
import com.YukiSato.KijinMod.item.tool.KijinPickaxe;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "kijinmod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(MobEntities.NINJA.get(), NinjaEntity.create().build());
        event.put(MobEntities.CAMERAMAN.get(), CameramanEntity.create().build());
    }

//    @ObjectHolder("yourmod:custom_pickaxe")
//    public static final Item CUSTOM_PICKAXE = null;
//
//    @SubscribeEvent
//    public static void onRegisterItems(RegistryObject.<Item> event) {
//        event.getRegistry().registerAll(
//                new KijinPickaxe()
//        );
//    }

//    @SubscribeEvent
//    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
//        event.register(MobEntities.NINJA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, NinjaEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
//    }
}
