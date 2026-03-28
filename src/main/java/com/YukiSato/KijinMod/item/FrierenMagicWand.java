package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.NonFireLB;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;

public class FrierenMagicWand extends BowItem {
    public static final Predicate<ItemStack> AMMO = stack -> stack.getItem() == Items.AIR;
    public int saru;
    public int we;
    public FrierenMagicWand() {
        super(new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }


    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return ItemStack.isSameItemSameTags(oldStack, newStack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return false;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Vec3 look = player.getLookAngle().normalize();
        Vec3 start = player.position()
                .add(0, player.getEyeHeight() * 0.6, 0)
                .add(look.scale(2.0));
        if (!level.isClientSide) {
            int steps = 50;
            float radius = 3F;

            for (int i = 0; i < steps; i++) {
                Vec3 pos = start.add(look.scale(i));
                level.explode(
                        null,
                        pos.x, pos.y, pos.z,
                        radius,
                        false,
                        Level.ExplosionInteraction.TNT
                );
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, true);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        Player player1 = (Player) entity;
        if (player1.isShiftKeyDown()) {
            player1.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 1, 6));
        }
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            AABB box3 = player1.getBoundingBox().inflate(2);

            List<Projectile> targets1 = level.getEntitiesOfClass(
                    Projectile.class,
                    box3,
                    p -> p.isAlive() && p.getOwner() != player1
            );

            if (!targets1.isEmpty()) {
                for (Projectile projectile : targets1) {

                    // ✅ 侵入した「瞬間」だけ処理（毎tickやらない）
                    var data = projectile.getPersistentData();
                    if (data.getBoolean("kijin_drop")) continue;
                    data.putBoolean("kijin_drop", true);


                    serverLevel.sendParticles(
                            ParticleTypes.ENCHANT,
                            projectile.getX(), projectile.getY(), projectile.getZ(),
                            25,
                            0.5, 0.5, 0.5,
                            0.01
                    );

                    level.playSound(
                            null,
                            projectile.getX(), projectile.getY(), projectile.getZ(),
                            SoundEvents.GLASS_BREAK,
                            SoundSource.PLAYERS,
                            1.0f,
                            0.9f + player1.getRandom().nextFloat() * 0.2f
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

        if (saru == 1) {
            double reach = 20.0; // 見る距離
            Vec3 eyePos = player1.getEyePosition();
            Vec3 look = player1.getLookAngle();
            Vec3 endPos = eyePos.add(look.scale(reach));

            AABB box1 = player1.getBoundingBox()
                    .expandTowards(look.scale(reach))
                    .inflate(1.0);

            EntityHitResult result = ProjectileUtil.getEntityHitResult(
                    player1.level(),
                    player1,
                    eyePos,
                    endPos,
                    box1,
                    entity1 -> entity1 instanceof LivingEntity && entity1 != player1
            );

            if (result != null && !level.isClientSide) {
                Entity target = result.getEntity();
                ServerLevel serverLevel = (ServerLevel) level;

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

                            BlockPos pos = target.blockPosition(); // 毎回取り直す
                            LightningBolt nonFire = new NonFireLB(serverLevel);
                            nonFire.moveTo(Vec3.atBottomCenterOf(pos));
                            nonFire.setCause(player1 instanceof ServerPlayer sp ? sp : null);
                            serverLevel.addFreshEntity(nonFire);
                        });
                    }
                }, 0, 500);
            }
        }


        if (KijinKeyBind.kijinKey[6].isDown()) {
            if (level.isClientSide) return;
            if (!(entity instanceof Player player)) return;
            if (!isSelected) return;

//        CompoundTag tag = stack.getOrCreateTag();
//        if (!tag.getBoolean("saru")) return;
//
//        tag.putBoolean("saru", false);
//
//        if (player.getCooldowns().isOnCooldown(this)) return;
//        player.getCooldowns().addCooldown(this, 200);

            if (!(level instanceof ServerLevel serverLevel)) return;

            // 半径20ブロック以内の敵
            AABB box2 = player.getBoundingBox().inflate(30);
            List<LivingEntity> targets = level.getEntitiesOfClass(
                    LivingEntity.class,
                    box2,
                    (LivingEntity e) -> e.isAlive() && e != player && e instanceof Monster
            );

            if (targets.isEmpty()) return;

            // ✅ 発動演出：斬撃サウンド


            // 発動演出：プレイヤー前方に「スイープ」粒子（剣の薙ぎ払い）
//        Vec3 look = player.getLookAngle().normalize();
//        Vec3 sweepPos = player.position()
//                .add(0, player.getEyeHeight() * 0.6, 0)
//                .add(look.scale(1.2)); // 目の前あたり
            Vec3 vec3 = player.getLookAngle();
            var source = player.damageSources().playerAttack(player);
            for (LivingEntity livingEntity : targets) {
//                Float distance = livingEntity.distanceTo(player);
//                Float distaa = distance / 2;
                if (livingEntity instanceof Monster) {
                    Mob mob = (Mob) livingEntity;
                    livingEntity.randomTeleport(box2.maxX / 4 * 3, box2.maxY / 4 * 3, box2.maxZ / 4 * 3, true);
                    livingEntity.randomTeleport(box2.maxX / 4 * 2, box2.maxY / 4 * 2, box2.maxZ / 4 * 2, true);
                    livingEntity.randomTeleport(player.getX() + vec3.x * 3, player.getY() + 5, player.getZ() + vec3.z * 3, true);
                    mob.setNoAi(true);
                    mob.isNoGravity();
                    mob.getNoActionTime();
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            // サーバーメインスレッドで実行させる
                            serverLevel.getServer().execute(() -> {
                                if (livingEntity == null || !livingEntity.isAlive() || livingEntity.isRemoved()) {
                                    this.cancel();   // TimerTask止める
                                    timer.cancel();  // Timer止める
                                    return;
                                }

                                level.playSound(
                                        null,
                                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                                        SoundEvents.PLAYER_ATTACK_SWEEP,
                                        SoundSource.PLAYERS,
                                        1.0f,
                                        0.9f + player.getRandom().nextFloat() * 0.2f
                                );

                                serverLevel.sendParticles(
                                        ParticleTypes.SWEEP_ATTACK,
                                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                                        1,
                                        0, 0, 0,
                                        0
                                );

                                livingEntity.hurt(source, livingEntity.getMaxHealth() / 5.0f);

                                serverLevel.sendParticles(
                                        ParticleTypes.LANDING_LAVA,
                                        livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * 0.5, livingEntity.getZ(),
                                        30,
                                        0.5, 0.5, 0.5,
                                        0.07
                                );
                            });
                        }
                    }, 0, 300);
                }


            }

        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return AMMO;
    }
}
