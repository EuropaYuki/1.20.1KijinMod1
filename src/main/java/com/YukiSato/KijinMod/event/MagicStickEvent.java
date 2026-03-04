//package com.YukiSato.KijinMod.event;
//
//import com.YukiSato.KijinMod.item.MagicStick;
//import com.YukiSato.KijinMod.main.KijinMod;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.event.entity.living.LivingAttackEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = KijinMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class MagicStickEvent {
//    private static final double HALF_ANGLE_DEG = 85.0; // 170/2
//    private static final double DOT_THRESHOLD = Math.cos(Math.toRadians(HALF_ANGLE_DEG));
//
//    @SubscribeEvent
//    public static void onLivingAttack(LivingAttackEvent event) {
//        LivingEntity victim = event.getEntity();
//
//        if (!(victim instanceof Player player)) return;
//        if (player.level().isClientSide) return;
//
//        // 構えていないなら無視
//        if (!player.isUsingItem()) return;
//
//        // 使用中アイテムがこのWideShieldItemか
//        if (!(player.getUseItem().getItem() instanceof MagicStick)) return;
//
//        DamageSource source = event.getSource();
//
//        // “防ぎたくない”ものがあるならここで除外（例：奈落）
//        // if (source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
//        // if (source == player.damageSources().outOfWorld()) return;
//
//        // 攻撃が“前方から来たか”判定
//        if (!isFromFront(player, source)) return;
//
//        // ここで完全無効化
//        event.setCanceled(true);
//
//        // 盾っぽい副作用（任意）：ブロック音/腕の疲労/クールダウン等を入れたければここ
//        // player.getCooldowns().addCooldown(player.getUseItem().getItem(), 5);
//    }
//
//    private static boolean isFromFront(Player player, DamageSource source) {
//        // 攻撃元を推定：まず directEntity（矢・衝撃波など）→なければ entity（攻撃者）
//        Entity direct = source.getDirectEntity();
//        Entity attacker = source.getEntity();
//
//        Vec3 fromPos = null;
//
//        if (direct != null) {
//            fromPos = direct.position();
//        } else if (attacker != null) {
//            fromPos = attacker.position();
//        } else {
//            // 攻撃元が特定できない（爆発や環境等）→「全部防ぐ」なら true にする
//            // “前方限定”にしたいなら false にする
//            return false;
//        }
//
//        Vec3 toVictim = player.position().subtract(fromPos).normalize(); // 攻撃→プレイヤー方向
//        Vec3 look = player.getLookAngle().normalize();                  // プレイヤー視線方向
//
//        // 視線と「攻撃が飛んできた方向」が近ければ前方
//        double dot = look.dot(toVictim);
//        return dot > DOT_THRESHOLD;
//    }
//}
