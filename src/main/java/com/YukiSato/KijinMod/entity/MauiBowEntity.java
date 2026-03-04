package com.YukiSato.KijinMod.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class MauiBowEntity extends AbstractArrow {
    private SoundEvent hitSound = SoundEvents.DRAGON_FIREBALL_EXPLODE;
    private int life;
    private int knockback;
    private List<Entity> piercedAndKilledEntities;
    ItemStack shootStack = new ItemStack(Items.AIR);
    private double damage = 0.4D;
    public MauiBowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public MauiBowEntity(Level level, LivingEntity entity, ItemStack stack) {
        super(KijinEntityTypes.MAUI_BOW.get(), entity, level);
        this.shootStack = stack;
    }

    public void setBaseDamage(double p_36782_) {
        this.damage = p_36782_;
    }

    public double getBaseDamage() {
        return this.damage;
    }

    public void tickDespawn() {
        ++this.life;
        if (this.life >= 60) {
            this.discard();
        }
    }

    public void setKnockback(int p_36736_) {
        this.knockback = p_36736_;
    }

    public int getKnockback() {
        return this.knockback;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        Entity entity = p_36757_.getEntity();

        if (entity.getType() == EntityType.ENDER_DRAGON) {
            setBaseDamage(getBaseDamage() * 2);
        }

        float f = (float)this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double)f * this.damage, 0.0D, (double)Integer.MAX_VALUE));

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

            this.playSound(this.hitSound, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
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
    public void setEnchantmentEffectsFromEntity(LivingEntity p_36746_, float p_36747_) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, p_36746_);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, p_36746_);
        this.setBaseDamage((double)(p_36747_ * 2.0F) + this.random.triangle((double)this.level().getDifficulty().getId() * 0.11D, 0.57425D));
        if (i > 0) {
            this.setBaseDamage(this.getBaseDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0) {
            this.setKnockback(j);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, p_36746_) > 0) {
            this.setSecondsOnFire(100);
        }

    }

      @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        BlockPos pos = entity.blockPosition();
        LightningBolt nonFire = new NonFireLB(entity.level());
        nonFire.moveTo(Vec3.atBottomCenterOf(pos));
        Player player = Minecraft.getInstance().player;
        nonFire.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
        entity.level().addFreshEntity(nonFire);
        nonFire.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
        entity.level().addFreshEntity(nonFire);
        Vec3 vec3 = player.getLookAngle();
        if (entity.getType() == EntityType.ENDERMAN) {
            entity.randomTeleport(player.getX() + vec3.x * 1, player.getY(), player.getZ() + vec3.z * 1, true);
            entity.hurt(entity.damageSources().playerAttack(player), 40);
        }
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 60 * 3, 7, true, true));
        entity.push(5, 6, 5);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        BlockState state = this.level().getBlockState(p_36755_.getBlockPos());
        state.onProjectileHit(this.level(), state, p_36755_, this);
        Vec3 vec3 = p_36755_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.playSound(hitSound, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 3;
        this.setCritArrow(false);
        this.setPierceLevel((byte)0);

        if (!this.level().isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), entity)) {
                BlockPos blockpos = p_36755_.getBlockPos().relative(p_36755_.getDirection());
                if (this.level().isEmptyBlock(blockpos)) {
                    this.level().setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level(), blockpos));
                }
            }

        }

    }

    @Override
    protected ItemStack getPickupItem() {
        return shootStack;
    }
}
