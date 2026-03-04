package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.effect.KijinEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlayerRenderHandler {
    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre event) {
        // ここに透明化を制御する条件を記述
        if (shouldBecomeInvisible(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    private static boolean shouldBecomeInvisible(Player player) {
        // 透明化する条件、例えば特定のアイテムを持っている、特定の効果が適用されているなど
        return player.hasEffect(KijinEffects.INVISIBILITY_EFFECT.get());
    }
}
