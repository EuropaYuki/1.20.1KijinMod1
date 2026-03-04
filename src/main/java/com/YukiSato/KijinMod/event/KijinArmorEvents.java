package com.YukiSato.KijinMod.event;


import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
import com.YukiSato.KijinMod.item.armor.ItemKijinChestplate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.model.EntityModel;
import org.lwjgl.glfw.GLFW;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "kijinmod", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KijinArmorEvents {

    private static NinjaEntity saru;

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        LivingEntity sa = event.getEntity();
        final DamageSource[] damageSources = {
                sa.damageSources().mobAttack(saru),
                sa.damageSources().magic(),
                sa.damageSources().wither(),
                sa.damageSources().fall(),
                sa.damageSources().flyIntoWall(),
                sa.damageSources().lightningBolt(),
                sa.damageSources().stalagmite(),
                sa.damageSources().fallingStalactite(sa)
        };




        for (DamageSource source : damageSources) {
            if (ItemKijinChestplate.set == 1 && event.getSource() == source) {
                event.setAmount(0);
            }
        }
        if (ItemKijinChestplate.set == 1) {

        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {

        List<Entity> affectedEntities = event.getAffectedEntities();

        // 影響を受けるエンティティのリストを反復処理
        Iterator<Entity> iterator = affectedEntities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            // エンティティがプレイヤーの場合はリストから削除
            if (entity instanceof Player) {
                iterator.remove();
            }
        }
    }






//    @SubscribeEvent
//    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
//        // アーマーのレンダリングをカスタマイズする前の処理
//        // 透明化を開始
//        GlStateManager.pushMatrix();
//        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.5F); // 透明度を半分に設定
//        GlStateManager.enableBlend(); // ブレンドを有効化
//        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA); // 透明度のブレンド方法を設定
//    }
//
//    @SubscribeEvent
//    public void onRenderPlayer(RenderPlayerEvent.Post event) {
//        // アーマーのレンダリングをカスタマイズした後の処理
//        // 透明化を終了
//        GlStateManager.disableBlend(); // ブレンドを無効化
//        GlStateManager.popMatrix();
//    }

    @SubscribeEvent
    public static void onNinjaEntityHurt(LivingHurtEvent event) {
        if (event.getEntity().getType() != MobEntities.NINJA.get()) {
            return;
        }
        LivingEntity sa = event.getEntity();
        final DamageSource[] damageSources = {
                sa.damageSources().magic(),
                sa.damageSources().wither(),
                sa.damageSources().fall(),
                sa.damageSources().flyIntoWall(),
        };




        for (DamageSource source : damageSources) {
            if (event.getSource() == source) {
                event.setAmount(0);
            }
        }
    }

}
