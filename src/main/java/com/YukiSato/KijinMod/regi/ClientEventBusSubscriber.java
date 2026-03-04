package com.YukiSato.KijinMod.regi;


import com.YukiSato.KijinMod.entity.KijinEntityTypes;
import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.entity.client.CameramanRender;
import com.YukiSato.KijinMod.entity.client.NinjaRender;
import com.YukiSato.KijinMod.entity.custom.CameramanEntity;
import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.main.KijinMod;
import com.YukiSato.KijinMod.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = KijinMod.MOD_ID, bus = Bus.MOD)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup (FMLClientSetupEvent event) {
        KijinKeyBind.register(event);
        ItemProperties.register(KijinModItems.KIJIN_BOW.get(), new ResourceLocation("pull"), (itemstack, level, entity, e) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != itemstack ? 0.0F : (float) (itemstack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20F;
            }
        });

        ItemProperties.register(KijinModItems.KIJIN_BOW.get(), new ResourceLocation("pulling"),
                (itemstack, level, entity, e) -> entity != null && entity.isUsingItem() && entity.getUseItem() == itemstack ? 1.0F : 0.0F);

    }

//    @SubscribeEvent
//    public static void commonSetup(FMLCommonSetupEvent event) {
//        registerSpawnPlacement();
//    }

    @SubscribeEvent
    public static void saru (FMLClientSetupEvent event) {
        ItemProperties.register(KijinModItems.MAUI_BOW.get(), new ResourceLocation("pull"), (itemstack, level, entity, e) ->{
            if (entity == null) {
                return 0.0F;
            } else {return entity.getUseItem() != itemstack ? 0.0F : (float) (itemstack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20F;}
        });

        ItemProperties.register(KijinModItems.MAUI_BOW.get(), new ResourceLocation("pulling"),
                (itemstack, level, entity, e) -> entity != null && entity.isUsingItem() && entity.getUseItem() == itemstack ? 1.0F : 0.0F);

    }
    @SubscribeEvent
    public static void renderRegistry (EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(KijinEntityTypes.DEMONS_BOLG.get(), DemonsBolgRender::new);
        event.registerEntityRenderer(KijinEntityTypes.KIJIN_AMMO.get(), KijinAmmoRender::new);
        event.registerEntityRenderer(KijinEntityTypes.MAUI_BOW.get(), MauiBowRender::new);
        event.registerEntityRenderer(KijinEntityTypes.DEATH_BEAM.get(), GunRender::new);
        event.registerEntityRenderer(KijinEntityTypes.SPERCKER.get(), SperckerRender::new);
        event.registerEntityRenderer(KijinEntityTypes.LAZER.get(), LazerRender::new);
        event.registerEntityRenderer(KijinEntityTypes.SENSAR.get(), SensarEntityRender::new);
    }

    @SubscribeEvent
    public static void ni(FMLClientSetupEvent event) {
        EntityRenderers.register(MobEntities.NINJA.get(), NinjaRender::new);
        EntityRenderers.register(MobEntities.CAMERAMAN.get(), CameramanRender::new);
    }

//    private static void registerSpawnPlacement () {
//        SpawnPlacements.register(MobEntities.NINJA.get(),
//                SpawnPlacements.Type.ON_GROUND,
//                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
//                NinjaEntity::canSpawn);
//    }

}
