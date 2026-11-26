package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.entity.NonFireLB;
import com.YukiSato.KijinMod.entity.RocketLauncherEntity;
import com.YukiSato.KijinMod.entity.SensarEntity;
import com.YukiSato.KijinMod.item.ExtraRarity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Katana extends SwordItem {
    public Katana() {
        super(KijinModTiers.SAKURITE, 10, -4.0F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player) entity;
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.KATANA.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 100, 3, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 4, false, false));
        }
        if (KijinKeyBind.kijinKey[7].consumeClick() && player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.KATANA.get()) {
            BlockPos origin = player.getOnPos();
            double radius = 40.0;
            AABB searchArea = new AABB (
                    origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius,
                    origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius
            );
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchArea);
            for (int i = 0; i < entities.size(); i++) {
                LivingEntity e = entities.get(i);
                if (e.getClassification(true) == MobCategory.MONSTER) {
                    while (e.getHealth() > 0) {
                        BlockPos pos = e.blockPosition();
                        if (e instanceof Mob) {
                            ((Mob) e).setNoAi(true);
                            ((Mob) e).getNavigation().stop();
                        }
                        LightningBolt nonFire = new NonFireLB(level);
                        nonFire.moveTo(Vec3.atBottomCenterOf(pos));
                        nonFire.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
                        level.addFreshEntity(nonFire);
                    }
                }
            }
            player.getCooldowns().addCooldown(this, 1200);
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        AABB area = new AABB(
                player.getX() - 1, player.getEyeY() - 1, player.getZ() - 1,
                player.getX() + 1, player.getEyeY() + 1, player.getZ() + 1
        );
        List<LivingEntity> targets = entity.level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                entity1 -> entity1 != player && entity1.isAlive()
        );
        for (LivingEntity target: targets) {
            if (entity.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.LAVA,
                        target.getX(),
                        target.getY(),
                        target.getZ(),
                        1,
                        0, 0, 0,
                        0
                );
            }

            target.hurt(target.damageSources().playerAttack(player), 20.0F);
        }
        int[] a = {-2, -1, 0, 1, 2};
        for (int ax = 0; ax < 5; ax++) {
            for (int az = 0; az < 5; az++) {
                BlockPos aPos = new BlockPos(
                        (int) player.getX() + a[ax],
                        (int) player.getEyeY(),
                        (int) player.getZ() + a[az]
                );

                if (entity.level().getBlockState(aPos).isAir() && entity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.PORTAL,
                            aPos.getX(),
                            aPos.getY(),
                            aPos.getZ(),
                            1,
                            0.2, 0.2, 0.2,
                            0.01
                    );
                    serverLevel.sendParticles(
                            ParticleTypes.SWEEP_ATTACK,
                            aPos.getX(),
                            aPos.getY(),
                            aPos.getZ(),
                            1,
                            0, 0, 0,
                            0
                    );
                }
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            AABB box3 = player.getBoundingBox().inflate(2);

            List<Projectile> targets1 = level.getEntitiesOfClass(
                    Projectile.class,
                    box3,
                    p -> p.isAlive() && p.getOwner() != player
            );

            if (!targets1.isEmpty()) {
                for (Projectile projectile : targets1) {

                    // ✅ 侵入した「瞬間」だけ処理（毎tickやらない）
                    var data = projectile.getPersistentData();
                    if (data.getBoolean("kijin_drop")) continue;
                    data.putBoolean("kijin_drop", true);

                    level.playSound(
                            null,
                            player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ANVIL_PLACE,
                            SoundSource.PLAYERS,
                            1.0f,
                            0.9f + player.getRandom().nextFloat() * 0.2f
                    );
                    // 効果音（1回）


                    // 赤フラッシュ（1回）


                    // ✅ 落とす：重力ON + 下向き初速を1回だけ付与
                    projectile.setNoGravity(false);
                    projectile.setDeltaMovement(0.0, -0.35, 0.0);
                    projectile.hurtMarked = true;

                    // 種類によっては物理無効があるので保険（コンパイル通る環境なら）
                    projectile.noPhysics = false;

                }
            }
        }
        double reach = 1; // 見る距離
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 endPos = eyePos.add(look.scale(reach));

        AABB box1 = player.getBoundingBox()
                .expandTowards(look.scale(reach))
                .inflate(1.0);
        EntityHitResult result = ProjectileUtil.getEntityHitResult(
                player.level(),
                player,
                eyePos,
                endPos,
                box1,
                entity1 -> entity1 instanceof LivingEntity
        );
        if (result != null) {
            LivingEntity entity1 = (LivingEntity) result.getEntity();
            entity1.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1, 999, false, false));
            Vec3 knockback = entity1.position().subtract(player.position()).normalize().scale(0.2);
            entity1.setDeltaMovement(entity1.getDeltaMovement().add(knockback));
        }
        return InteractionResultHolder.success(stack);
    }


}
