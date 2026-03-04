package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.RocketLauncherEntity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class RockRocketLauncher extends BowItem {
    public static final Predicate<ItemStack> AMMO = stack -> stack.getItem() == Items.AIR;
    public RockRocketLauncher() {
        super(new Properties().rarity(ExtraRarity.ULTIMATE).fireResistant().durability(2));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        Player player = (Player) entity;
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.ROCK_ROCKET_LAUNCHER.get()) {
            if (!world.isClientSide && stack.getDamageValue() == 1) {
                player.getCooldowns().addCooldown(this, 100);
                stack.setDamageValue(stack.getDamageValue() - 10);
            }
            if (!world.isClientSide && KijinKeyBind.kijinKey[7].consumeClick()) {
                this.scopeMode(stack);
                world.playSound(null, player.getOnPos(), SoundEvents.ITEM_FRAME_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 100, 99, false, false));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Vec3 vec3 = player.getLookAngle();
        ItemStack itemStack = player.getProjectile(stack);
        if (!level.isClientSide && itemStack.getItem() == Items.AIR) {
            ItemStack ammo = new ItemStack(Items.AIR);
            AbstractArrow ammoEntity = new RocketLauncherEntity(level, player, ammo);
            ammoEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 20.0F, 0.0F);
            level.addFreshEntity(ammoEntity);
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            stack.hurtAndBreak(1, player, b-> {
            });
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.consume(stack);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_FRAME_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResultHolder.fail(stack);
    }

    public void scopeMode(ItemStack stack) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt("rocket_scope", this.modeInt(stack) == 0 ? 1 : 0);
    }

    public int modeInt (ItemStack stack) {
        if (stack.getTag() == null) {
            return 0;
        }
        return stack.getTag().getInt("rocket_scope");
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return AMMO;
    }
}
