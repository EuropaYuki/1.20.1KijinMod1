package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "kijinmod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

//    @SubscribeEvent
//    public static void entityAttributes(EntityAttributeCreationEvent event) {
//        event.put(MobEntities.NINJA.get(), NinjaEntity.create().build());
//    }
}
