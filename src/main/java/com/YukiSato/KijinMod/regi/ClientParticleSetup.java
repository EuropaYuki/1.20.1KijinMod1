package com.YukiSato.KijinMod.regi;

import com.YukiSato.KijinMod.particle.KamehameParticle;
import com.YukiSato.KijinMod.regi.particle.KijinParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientParticleSetup {
//    @SubscribeEvent
//    public static void onClientSetup(FMLClientSetupEvent event) {
//        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
//        particleEngine.register(KijinParticles.KAMEHAME_PARTICLE.get(), new KamehameParticle.Factory(spriteSet));
//    }
}
