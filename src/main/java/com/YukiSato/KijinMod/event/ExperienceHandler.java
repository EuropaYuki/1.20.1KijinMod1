package com.YukiSato.KijinMod.event;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ExperienceHandler {

//    @SubscribeEvent
//    public static void onLivingDeath(LivingDeathEvent event) {
//        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof Monster) {
//            // エンティティがモンスターの場合のみ処理
//            Monster monster = (Monster) event.getEntity();
//            for (int i = 0; i < 3; i++) {
//                event.getEntity().level().addFreshEntity(new ExperienceOrb(event.getEntity().level(), monster.getX(), monster.getY(), monster.getZ(), Integer.MAX_VALUE));
//            }
//        }
//    }
}
