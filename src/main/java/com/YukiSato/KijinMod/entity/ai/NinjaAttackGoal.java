package com.YukiSato.KijinMod.entity.ai;

import com.YukiSato.KijinMod.entity.KijinAmmoEntity;
import com.YukiSato.KijinMod.entity.MauiBowEntity;
import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
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

                    if (this.attackStep > 0.000000000000000000000000000001) {
                        double d4 = Math.sqrt(Math.sqrt(d0));
                        if (!this.mob.isSilent()) {
                            this.mob.level().levelEvent((Player) null, 1018, this.mob.blockPosition(), 0);
                        }

                        Vec3 vec3 = mob.getViewVector(1.0F);
                        double d5 = vec3.x;
                        double d6 = vec3.y;
                        double d7 = vec3.z;
                        //double d4 = Math.sqrt(Math.sqrt(d0)) * 0.5D;

                        ItemStack ammo = new ItemStack(Items.AIR);



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
