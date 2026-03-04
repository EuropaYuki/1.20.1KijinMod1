package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "kijinmod", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KijinGunEvents {

    @SubscribeEvent
    public static void scopeMode(ComputeFovModifierEvent event) {
        ItemStack stack = event.getPlayer().getMainHandItem();
        if (stack.getItem() == KijinModItems.KIJIN_GUN.get() && stack.getTag().getInt("scope") == 1) {
            event.setNewFovModifier(0.3F);
        }
        if (event.getNewFovModifier() == 0.3F) {
            Minecraft.getInstance().options.sensitivity().set(0.3D);
        } else {
            Minecraft.getInstance().options.sensitivity().set(0.5D);
        }
    }

    @SubscribeEvent
    public static void rocketScopeMode(ComputeFovModifierEvent event) {
        ItemStack stack = event.getPlayer().getMainHandItem();
        if (stack.getItem() == KijinModItems.ROCK_ROCKET_LAUNCHER.get() && stack.getTag().getInt("rocket_scope") == 1) {
            event.setNewFovModifier(0.3F);
        }
        if (event.getNewFovModifier() == 0.3F) {
            Minecraft.getInstance().options.sensitivity().set(0.3D);
        } else {
            Minecraft.getInstance().options.sensitivity().set(0.5D);
        }
    }

}
