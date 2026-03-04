package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.DeathBeamEntity;
import com.YukiSato.KijinMod.entity.KijinAmmoEntity;
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

public class MatchlockGun extends BowItem {

    public static final Predicate<ItemStack> AMMO = stack -> stack.getItem() == Items.AIR;
    public MatchlockGun() {
        super(new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE).durability(2));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player) entity;
        if (!level.isClientSide && KijinKeyBind.kijinKey[4].consumeClick()) {
            this.scopeMode(stack);
            level.playSound(null, player.getOnPos(), SoundEvents.ITEM_FRAME_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        if (!level.isClientSide && stack.getDamageValue() == 1) {
            player.getCooldowns().addCooldown(this, 600);
            stack.setDamageValue(stack.getDamageValue() - 2);
        }
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.KIJIN_GUN.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 5, 0, false, false));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Vec3 vec3 = player.getLookAngle();
        ItemStack itemStack = player.getProjectile(stack);
        if (!level.isClientSide && itemStack.getItem() == Items.AIR) {
            ItemStack ammo = new ItemStack(Items.AIR);
            AbstractArrow ammoEntity = new DeathBeamEntity(level, player, ammo);
            ammoEntity.shoot(vec3.x, vec3.y, vec3.z, 45F, 0F);
            level.addFreshEntity(ammoEntity);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            stack.hurtAndBreak(1, player, (b) ->{
                b.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
            return InteractionResultHolder.consume(stack);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_FRAME_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResultHolder.fail(stack);
    }

    public void scopeMode(ItemStack stack) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt("scope", this.modeInt(stack) == 0 ? 1 : 0);
    }

    public int modeInt (ItemStack stack) {
        if (stack.getTag() == null) {
            return 0;
        }
        return stack.getTag().getInt("scope");
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return AMMO;
    }
}
