package com.YukiSato.KijinMod.entity.ai;

import com.YukiSato.KijinMod.entity.KijinAmmoEntity;
import com.YukiSato.KijinMod.entity.MauiBowEntity;
import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NinjaAttackGoal extends MeleeAttackGoal {

    private int fireballCooldown = 0;
    private final NinjaEntity entity;
    private int attackDelay = 0;
    private int nextAttack = 0;
    private boolean tillNextAttack = false;
    private int attackStep;
    private int attackTime;
    private int lastSeen;
    public NinjaAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        entity = ((NinjaEntity) mob);
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity entity, double d) {
        return d <= this.getAttackReachSqr(entity);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity entity, double d) {
        if (isEnemyWithinAttackDistance(entity, d)){
            tillNextAttack = true;
            if (isTimeToAttackStart()) {
                this.entity.setAttacking(true);
            }
            if (isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(entity.getX(), entity.getY(), entity.getZ());
                performAttack(entity);
            }
        } else {
            resetAttackCooldown();
            tillNextAttack = false;
            this.entity.setAttacking(false);
        }

    }

    protected boolean isTimeToAttackStart() {
        return this.nextAttack <= attackDelay;
    }
    protected void resetAttackCooldown() {
        this.nextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    protected boolean isTimeToAttack() {
        return this.nextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.nextAttack;
    }

    protected void performAttack(LivingEntity entity) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(entity);
    }



    @Override
    public void start() {
        super.start();
        attackDelay = 0;
        nextAttack = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (tillNextAttack) {
            this.nextAttack = Math.max(this.nextAttack - 1, 0);
        }

        --this.attackTime;
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
            if (flag) {
                this.lastSeen = 0;
            } else {
                ++this.lastSeen;
            }

            double d0 = this.mob.distanceToSqr(livingentity);
            if (d0 < 4.0D) {
                if (!flag) {
                    return;
                }

                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.mob.doHurtTarget(livingentity);
                }

                this.mob.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
            } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                double d1 = livingentity.getX() - this.mob.getX();
                double d2 = livingentity.getY() - this.mob.getY();
                double d3 = livingentity.getZ() - this.mob.getZ();
                if (this.attackTime <= 0) {
                    ++this.attackStep;
                    if (this.attackStep == 1) {
                        this.entity.setCharged(true);
                        this.attackTime = 60;
                    } else if (this.attackStep <= 4) {

                        this.attackTime = 6;
                    } else {
                        entity.setCharged(false);
                        this.attackTime = 100;
                        this.attackStep = 0;

                    }

                    if (fireballCooldown <= 0) {

                        LivingEntity target = this.entity.getTarget();

                        if (target != null) {
                            this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                            this.entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
                        }
                        Vec3 vec3 = entity.getLookAngle();

                        if (!this.entity.isSilent()) {
                            this.entity.level().levelEvent((Player)null, 1018, this.entity.blockPosition(), 0);
                        }
                        ItemStack stack = new ItemStack(Items.AIR);
                        KijinAmmoEntity ammo = new KijinAmmoEntity(entity.level(), entity, stack);
                        ammo.setPos(
                                this.entity.getX() + vec3.x * 1.0D,
                                this.entity.getEyeY() - 0.1D,
                                this.entity.getZ() + vec3.z * 1.0D
                        );

                        // shoot(x, y, z, speed, inaccuracy) 形式を想定
                        ammo.shoot(vec3.x, vec3.y, vec3.z, 1.5F, 0.0F);


                        this.entity.level().addFreshEntity(ammo);

                        // ★3秒クールタイム設定
                        fireballCooldown = 60; // 20tick × 3秒
                    }
                }

                this.mob.getLookControl().setLookAt(livingentity, 0F, 0F);
            } else if (this.lastSeen < 5) {
                this.mob.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 0D);
            }

            super.tick();
        }

    }




    private double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }



    @Override
    public void stop() {
        entity.setAttacking(false);
        entity.setCharged(false);
        super.stop();
    }
}
