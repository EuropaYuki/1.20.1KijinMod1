package com.YukiSato.KijinMod.main;

import com.YukiSato.KijinMod.effect.KijinEffects;
import com.YukiSato.KijinMod.entity.KijinEntityTypes;
import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.event.MeteorEventHandler;
import com.YukiSato.KijinMod.regi.KijinModBlocks;
import com.YukiSato.KijinMod.regi.KijinModItems;
import com.YukiSato.KijinMod.regi.particle.KijinParticles;
import com.YukiSato.KijinMod.regi.tab.KijinModTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod("kijinmod")
public class KijinMod {
    private long lastSpaceTime = 0;
    private boolean isFlying = false;


    public static final String MOD_ID = "kijinmod";

    public KijinMod () {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        KijinEntityTypes.REGISTER.register(bus);
        KijinModTabs.MOD_TABS.register(bus);
        MobEntities.register(bus);
        KijinModBlocks.Blocks.BLOCKS.register(bus);
        KijinModBlocks.BlockItems.BLOCK_ITEMS.register(bus);
        KijinModItems.ITEMS.register(bus);
        KijinParticles.PARTICLE_TYPES.register(bus);
        KijinEffects.EFFECTS.register(bus);
        MinecraftForge.EVENT_BUS.register(new MeteorEventHandler());
    }
}
