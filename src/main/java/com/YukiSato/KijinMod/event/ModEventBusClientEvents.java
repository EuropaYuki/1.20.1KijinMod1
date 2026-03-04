package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.entity.client.CameraManModel;
import com.YukiSato.KijinMod.entity.client.MobModelLayers;
import com.YukiSato.KijinMod.entity.client.NinjaModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "kijinmod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MobModelLayers.NINJA_LAYER, NinjaModel::createBodyLayer);
        event.registerLayerDefinition(MobModelLayers.CAMERAMAN_LAYER, CameraManModel::createBodyLayer);
    }
}
