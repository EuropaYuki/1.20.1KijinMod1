package com.YukiSato.KijinMod.event;

import com.YukiSato.KijinMod.main.KijinMod;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KijinMod.MOD_ID)
public class ShieldEvents {

    private static final double R = 2.5;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.level().isClientSide) return;

        // シールドON？

        // 杖を持ってる？
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() != KijinModItems.MAGIC_STICK.get()) return;

        // 「直接当ててきたもの」が投射物なら
        if (!(event.getSource().getDirectEntity() instanceof Projectile proj)) return;

        // 半径チェック（任意。近接着弾でも発動させたいなら消してOK）
        Vec3 center = player.position().add(0, player.getBbHeight() * 0.55, 0);
        if (proj.position().distanceTo(center) > R) return;

        // ✅ まずダメージを無効化
        event.setCanceled(true);

        // ✅ 反射（プレイヤー中心から外へ押し出す）
        Vec3 p = proj.position();
        Vec3 outward = p.subtract(center).normalize();
        double speed = Math.max(0.8, proj.getDeltaMovement().length() * 1.05);

        proj.setDeltaMovement(outward.scale(speed));
        proj.hurtMarked = true;

        // 任意：跳ね返した扱いにする
        proj.setOwner(player);
    }
}
