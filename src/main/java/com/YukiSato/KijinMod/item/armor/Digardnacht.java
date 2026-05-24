package com.YukiSato.KijinMod.item.armor;

import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Digardnacht extends ArmorItem {

    public static int set;
    public Digardnacht() {
        super(KijinArmorMaterials.KIJIN, Type.CHESTPLATE, new Properties().fireResistant().rarity(Rarity.COMMON));
    }

    private boolean isWearingFullKijinArmor(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() == KijinModItems.DIGARDNACHT.get();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        Player player = (Player) entity;
        for (int i = 0; i < 2; ++i) {
            level.addParticle(ParticleTypes.PORTAL, player.getRandomX(0.5D), player.getRandomY() - 0.25D, player.getRandomZ(0.5D), (level.random.nextDouble() - 0.5D) * 2.0D, -level.random.nextDouble(), (level.random.nextDouble() - 0.5D) * 2.0D);
        }

//        if (KijinKeyBind.kijinKey[7].isDown()) {
//            double reach = 3; // 見る距離
//            Vec3 eyePos = player.getEyePosition();
//            Vec3 look = player.getLookAngle();
//            Vec3 endPos = eyePos.add(look.scale(reach));
//
//            AABB box1 = player.getBoundingBox()
//                    .expandTowards(look.scale(reach))
//                    .inflate(3.0);
//            EntityHitResult result = ProjectileUtil.getEntityHitResult(
//                    player.level(),
//                    player,
//                    eyePos,
//                    endPos,
//                    box1,
//                    entity1 -> entity1 instanceof LivingEntity
//            );
//            if (result != null && level instanceof ServerLevel serverLevel) {
//                Entity entity1 = result.getEntity();
//                Vec3 knockback = entity1.position().subtract(player.position()).normalize().scale(0.2);
//                entity1.setDeltaMovement(entity1.getDeltaMovement().add(knockback));
//                serverLevel.sendParticles(
//                        ParticleTypes.SMOKE,
//                        entity1.getX(),
//                        entity1.getY(),
//                        entity1.getZ(),
//                        10,
//                        0.2, 0.2, 0.2,
//                        0.01
//                );
//            }
//        }
        if (isWearingFullKijinArmor(player)) {
            set = 1;
            applyPotionEffects(player);
            player.removeEffect(MobEffects.DARKNESS);

            Vec3 vec3 = player.getLookAngle();
            if (KijinKeyBind.kijinKey[0].isDown()) {
                player.setDeltaMovement(vec3.x * 2, vec3.y * 2, vec3.z * 2);
            }
            if (KijinKeyBind.kijinKey[1].isDown()) {
                // ■ 範囲（5x5）
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 11));
                AABB area = new AABB(
                        player.getX() - 1, player.getEyeY() - 1, player.getZ() - 1,
                        player.getX() + 1, player.getEyeY() + 1, player.getZ() + 1
                );

                // ■ 敵取得
                List<LivingEntity> targets = level.getEntitiesOfClass(
                        LivingEntity.class,
                        area,
                        entity1 -> entity1 != player && entity1.isAlive()
                );

                // ■ ダメージ処理
                for (LivingEntity target : targets) {

                    // ダメージ
                    target.invulnerableTime = 0;
                    target.hurt(player.damageSources().playerAttack(player), 7.0F);

                    // ノックバック（斬撃っぽさ）
                    Vec3 knockback = target.position().subtract(player.position()).normalize().scale(0.1);
                    target.setDeltaMovement(target.getDeltaMovement().add(knockback));
                }

                // ■ 既存のパーティクル（そのまま）
                int[] a = {-2, -1, 0, 1, 2};
                for (int ax = 0; ax < 5; ax++) {
                    for (int az = 0; az < 5; az++) {
                        BlockPos aPos = new BlockPos(
                                (int) player.getX() + a[ax],
                                (int) player.getEyeY(),
                                (int) player.getZ() + a[az]
                        );

                        if (level.getBlockState(aPos).isAir() && level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(
                                    ParticleTypes.SMOKE,
                                    aPos.getX(),
                                    aPos.getY(),
                                    aPos.getZ(),
                                    10,
                                    0.2, 0.2, 0.2,
                                    0.01
                            );
                            serverLevel.sendParticles(
                                    ParticleTypes.SWEEP_ATTACK,
                                    aPos.getX(),
                                    aPos.getY(),
                                    aPos.getZ(),
                                    10,
                                    0, 0, 0,
                                    0
                            );
                        }
                    }
                }
                player.setDeltaMovement(vec3.x * 1.1, vec3.y * 1.1, vec3.z * 1.1);
            }
        } else {
            if (player.getAbilities().mayfly && !player.isCreative()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                set = 0;
            }
        }
    }

    private void applyPotionEffects(Player player) {
        if (isWearingFullKijinArmor(player) == true) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 6, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, false));
        }
    }


    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return set == 1;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return true;
    }
}
