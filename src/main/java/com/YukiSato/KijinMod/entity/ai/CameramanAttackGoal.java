package com.YukiSato.KijinMod.entity.ai;

import com.YukiSato.KijinMod.entity.DeathBeamEntity;
import com.YukiSato.KijinMod.entity.KijinAmmoEntity;
import com.YukiSato.KijinMod.entity.custom.CameramanEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class CameramanAttackGoal extends MeleeAttackGoal {

    private final CameramanEntity entity;
    private int attackDelay = 1;
    private int ticksUntilNextAttack = 1;
    private boolean shouldContTillNextAttack = false;
    private int attackStep;
    private int attackTime;
    private int lastSeen;
    private int nextAttack = 0;
    private boolean tillNextAttack = false;
    public CameramanAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        entity = ((CameramanEntity) mob);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        if (isEnemyWithinAttackDistance(enemy, distToEnemySqr)) {
            shouldContTillNextAttack = true;
            entity.setAttacking(true);
            if (isTimeToAttack()) {
                mob.getLookControl().setLookAt(enemy.getX(), enemy.getY(), enemy.getZ());
                performAttack(enemy);
            }
        } else {
            resetAttackCooldown();
            shouldContTillNextAttack = false;
            entity.setAttacking(false);
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity enemy, double distToEnemySqr) {
        return distToEnemySqr <= this.getAttackReachSqr(enemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    protected void performAttack(LivingEntity enemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(enemy);
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 1;
        ticksUntilNextAttack = 1;
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
    }

    @Override
    public void tick() {
        super.tick();
        if (tillNextAttack) {
            this.nextAttack = Math.max(this.nextAttack - 1, 0);
        }

        --this.attackTime;
        if (mob.getTarget() instanceof Monster) {
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

                        if (this.attackStep > 1) {

                            Vec3 vec3 = mob.getViewVector(1.0F);
                            double d5 = vec3.x;
                            double d6 = vec3.y;
                            double d7 = vec3.z;
                            //double d4 = Math.sqrt(Math.sqrt(d0)) * 0.5D;
                            ItemStack ammo = new ItemStack(Items.AIR);
                            AbstractArrow ammoEntity = new DeathBeamEntity(mob.level(), mob, ammo);
                            ammoEntity.shoot(d5, d6, d7, 45F, 0F);
                            mob.level().addFreshEntity(ammoEntity);


                        }
                    }
                    this.mob.getLookControl().setLookAt(livingentity, 0F, 0F);

                } else if (this.lastSeen < 5) {
                    this.mob.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 0D);
                }

                super.tick();
            }
        }


    }
    private double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }
}
