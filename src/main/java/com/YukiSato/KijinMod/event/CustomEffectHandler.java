package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.item.armor.ItemKijinChestplate;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "kijinmod")
public class CustomEffectHandler {

    @SubscribeEvent
    public static void onPotionEffectApply(PlayerBrewedPotionEvent event) {
        if (event.getEntity() instanceof Player) {  // イベントがプレイヤーに発生した場合のみ
            LivingEntity sa = event.getEntity();
            final MobEffect[] effects = {
                    MobEffects.DARKNESS
            };
            for (MobEffect source : effects) {
                if (ItemKijinChestplate.set == 1 && event.getEntity().getEffect(MobEffects.DARKNESS).getEffect() == source) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
