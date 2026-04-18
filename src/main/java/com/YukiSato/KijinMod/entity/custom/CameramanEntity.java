package com.YukiSato.KijinMod.entity.custom;


import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.entity.ai.CameramanAttackGoal;
import com.YukiSato.KijinMod.entity.ai.NinjaAttackGoal;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CameramanEntity extends TamableAnimal {

    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(NinjaEntity.class, EntityDataSerializers.BOOLEAN);
    public CameramanEntity(EntityType<? extends CameramanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CameramanAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(KijinModItems.KIJIN_BURGER.get()), false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 0, false, false, (entity) -> {
            return entity instanceof Mob && !(entity instanceof CameramanEntity) && !(entity instanceof NinjaEntity);
        }));
//        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (player) ->{
//            return player instanceof Player;
//        }));
    }

    @Override
    protected boolean canRide(Entity p_20339_) {
        return true;
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isOnFire() {
        return this.isCharged();
    }


    private boolean isCharged() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setCharged(boolean p_32241_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_32241_) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public void aiStep() {
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 1.0D, 1.0D));
        }

        super.aiStep();
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }


    public static AttributeSupplier.Builder create () {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1000D)
                .add(Attributes.FOLLOW_RANGE, 2000D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f)
                .add(Attributes.ATTACK_DAMAGE, 0f);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob p_146744_) {
        return MobEntities.CAMERAMAN.get().create(level);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(KijinModItems.KIJIN_BURGER.get());
    }
}
