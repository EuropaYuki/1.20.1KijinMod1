package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.entity.NonFireLB;
import com.YukiSato.KijinMod.item.ExtraRarity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Timer;
import java.util.TimerTask;

public class RedLightSaber extends SwordItem {

    public RedLightSaber() {
        super(KijinModTiers.SAKURITE, Integer.MAX_VALUE, -1.7F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    public UseAnim getUseAnimation(ItemStack p_43105_) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(ItemStack p_43107_) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level p_43099_, Player p_43100_, InteractionHand p_43101_) {
        ItemStack itemstack = p_43100_.getItemInHand(p_43101_);
        p_43100_.startUsingItem(p_43101_);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player)entity;
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 4, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 300, 4, false, false));
        if (KijinKeyBind.kijinKey[7].consumeClick()) {
            double reach = 3.5; // 見る距離
            Vec3 eyePos = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            Vec3 endPos = eyePos.add(look.scale(reach));

            AABB box1 = player.getBoundingBox()
                    .expandTowards(look.scale(reach))
                    .inflate(3.0);
            EntityHitResult result = ProjectileUtil.getEntityHitResult(
                    player.level(),
                    player,
                    eyePos,
                    endPos,
                    box1,
                    entity1 -> entity1 instanceof LivingEntity
            );
            if (result != null && !level.isClientSide) {
                Entity target = result.getEntity();
                ServerLevel serverLevel = (ServerLevel) level;
                if (target instanceof LivingEntity) {
                    ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 160, 0, false, false));
                    ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 50, false, false));
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            // サーバースレッドで実行
                            serverLevel.getServer().execute(() -> {
                                if (!target.isAlive()) { // 死んだら止める
                                    this.cancel();
                                    timer.cancel();
                                    return;
                                }
                                target.hurt(level.damageSources().freeze(), ((LivingEntity) target).getMaxHealth() / 10);

                            });
                        }
                    }, 0, 800);
                }
            }
        }
    }
}
