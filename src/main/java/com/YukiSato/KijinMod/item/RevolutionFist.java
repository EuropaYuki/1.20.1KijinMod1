package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.item.tool.KijinModTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class RevolutionFist extends AxeItem {

    public RevolutionFist() {
        super(KijinModTiers.SAKURITE, 3F, -2.9F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity entity) {
        Player player = (Player) entity;
        Level level = player.level();
        Vec3 playerPos = player.position();

        // 通常攻撃（神撃の一閃）
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(playerPos.add(-50, -50, -50), playerPos.add(50, 50, 50)));
        for (LivingEntity entity1 : entities) {
            if (entity1 != player) {
                entity1.setHealth(0.0F);
                entity1.knockback(10000.0F, player.getX() - entity.getX(), player.getZ() - entity.getZ());
                level.addParticle(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
            }
        }
        return super.hurtEnemy(stack, player, target);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR; // 武器のアニメーション（パンチ）
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true; // エンチャント可能
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof Player player)) return;

        // 無敵化

        // 速度強化
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 10, false, false));
        if (player.getHealth() < 45F) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 10, false, false));
        }
        // 恐怖のオーラ（敵が動けなくなる）
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // シフト + 右クリック（マジゴッド・エンド）
        if (player.isShiftKeyDown()) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(100));
            for (LivingEntity entity1 : entities) {
                if (entity1 != player) {
                    entity1.hurt(entity1.damageSources().outOfBorder(), Float.MAX_VALUE);
                }
            }

            // 全ブロックを虚無ブロックに変える（⚠️ 超危険）
            for (int x = -50; x <= 50; x++) {
                for (int y = -50; y <= 50; y++) {
                    for (int z = -50; z <= 50; z++) {
                        level.removeBlock(player.blockPosition().offset(x, y, z), false);
                    }
                }
            }

            // バルスエフェクト
            level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 10.0F, 1.0F);
            player.sendSystemMessage(Component.literal("バルス！！！！！！！！"));
        }
        return InteractionResultHolder.consume(stack);
    }
}
