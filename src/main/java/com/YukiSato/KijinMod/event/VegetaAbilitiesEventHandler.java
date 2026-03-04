package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.main.KijinMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KijinMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class VegetaAbilitiesEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        // 高速移動の実装
        if (player.isSprinting()) {
            player.getAbilities().setFlyingSpeed(0.5f); // 通常の飛行速度を上げる
        } else {
            player.getAbilities().setFlyingSpeed(0.05f); // 通常の飛行速度に戻す
        }

        // 強力なパンチの実装
        if (player.getMainHandItem().is(Items.AIR) && player.swinging) {
            player.getMainHandItem().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            // ダメージを増加させる処理をここに追加
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();

        // エネルギー波の実装
        if (itemStack.is(Items.AIR)) { // ダイヤモンドをエネルギー波のトリガーに設定
            // エネルギー波を発射する処理をここに追加
            player.level().explode(player, player.getX(), player.getY(), player.getZ(), 5.0f, Level.ExplosionInteraction.MOB);
            itemStack.shrink(1); // ダイヤモンドを1個消費
        }
    }

}
