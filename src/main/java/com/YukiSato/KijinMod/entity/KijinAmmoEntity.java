package com.YukiSato.KijinMod.entity;

import com.YukiSato.KijinMod.regi.KijinModItems;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class KijinAmmoEntity extends AbstractArrow {
    private double baseDamage = 0.3D;
    private int knockback;
    private SoundEvent soundEvent = SoundEvents.IRON_GOLEM_STEP;
    private ItemStack shootStack = new ItemStack(Items.AIR);
    @Nullable
    private List<Entity> piercedAndKilledEntities;
    private boolean exploded = false;
    public KijinAmmoEntity(EntityType<? extends KijinAmmoEntity> type, Level level) {
        super(type, level);
    }

    public KijinAmmoEntity(Level level, LivingEntity entity, ItemStack stack) {
        super(KijinEntityTypes.KIJIN_AMMO.get(), entity, level);
        this.shootStack = stack;
    }

    @Override
    public void setBaseDamage(double p_36782_) {
        this.baseDamage = p_36782_;
    }

    @Override
    public double getBaseDamage() {
        return this.baseDamage;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        Entity entity = p_36757_.getEntity();


        BlockPos pos = entity.blockPosition();
        if (!level().isClientSide) {
            int a [] = {-2, -1, 0, 1, 2};
            ServerLevel serverLevel = (ServerLevel) level();
            for (int ax = 0; ax < 5; ax++) {
                for (int az = 0; az < 5; az++) {
                    for (int y = 0; y < 1; y++) {
                        BlockPos aPos = new BlockPos(pos.getX() + a[ax], pos.getY() - y, pos.getZ() + a[az]);
                        entity.hurt(entity.damageSources().freeze(), 50F);
                        serverLevel.sendParticles(
                                ParticleTypes.SWEEP_ATTACK,
                                pos.getX() + a[ax], pos.getY() - y, pos.getZ() + a[az],
                                1,
                                0, 0, 0,
                                0
                        );
                        serverLevel.sendParticles(
                                ParticleTypes.LANDING_LAVA,
                                entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                                15,
                                0.5, 0.5, 0.5,
                                0.07
                        );
                        if (entity instanceof  LivingEntity) {
                            level().playSound(
                                    null,
                                    entity.getX(), entity.getY(), entity.getZ(),
                                    SoundEvents.PLAYER_ATTACK_SWEEP,
                                    SoundSource.PLAYERS,
                                    1.0f,
                                    0.9f + ((LivingEntity)entity).getRandom().nextFloat() * 0.2f
                            );
                        }

                    }
                }
            }
        }
        if (entity.getType() == EntityType.ENDER_DRAGON) {
            this.setBaseDamage(getBaseDamage() * 2);
        }
        float f = (float)this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double)f * this.baseDamage, 0.0D, (double)Integer.MAX_VALUE));

        if (this.isCritArrow()) {
            long j = (long)this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = this.damageSources().arrow(this, this);
        } else {
            damagesource = this.damageSources().arrow(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastHurtMob(entity);
            }
        }

        int k = entity.getRemainingFireTicks();

        if (entity.hurt(damagesource, (float)i)) {

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                if (!this.level().isClientSide && this.getPierceLevel() <= 0) {
                    livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                }

                if (this.knockback > 0) {
                    double d0 = Math.max(0.0D, 1.0D - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockback * 0.6D * d0);
                    if (vec3.lengthSqr() > 0.0D) {
                        livingentity.push(vec3.x, 0.1D, vec3.z);
                    }
                }

                if (!this.level().isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
                }

                this.doPostHurtEffects(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }
            }

            this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            entity.setRemainingFireTicks(k);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
        }

    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        BlockState state = this.level().getBlockState(result.getBlockPos());
        state.onProjectileHit(this.level(), state, result, this);
        Vec3 vec3 = result.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 0;
        this.setPierceLevel((byte)0);
        BlockPos pos = result.getBlockPos();
        if (!level().isClientSide) {
            int a [] = {-2, -1, 0, 1, 2};
            ServerLevel serverLevel = (ServerLevel) level();
            for (int ax = 0; ax < 5; ax++) {
                for (int az = 0; az < 5; az++) {
                    for (int y = 0; y < 1; y++) {
                        BlockPos aPos = new BlockPos(pos.getX() + a[ax], pos.getY() - y, pos.getZ() + a[az]);
                        level().destroyBlock(aPos, true);
                        serverLevel.sendParticles(
                                ParticleTypes.SWEEP_ATTACK,
                                pos.getX() + a[ax], pos.getY() - y, pos.getZ() + a[az],
                                1,
                                0, 0, 0,
                                0
                        );
                    }
                }
            }
        }

        this.discard();
    }


    @Override
    protected ItemStack getPickupItem() {
        return shootStack;
    }
}
