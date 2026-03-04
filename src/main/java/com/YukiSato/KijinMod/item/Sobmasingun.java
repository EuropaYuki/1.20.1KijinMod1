package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.DeathBeamEntity;
import com.YukiSato.KijinMod.regi.KijinModItems;
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

import java.util.function.Predicate;

public class Sobmasingun extends BowItem {
    public static final Predicate<ItemStack> AMMO = stack -> stack.getItem() == Items.AIR;
    private final int fireRate;

    public Sobmasingun(int fireRate) {
        super(new Properties().durability(30).fireResistant().rarity(ExtraRarity.ULTIMATE));
        this.fireRate = fireRate;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player) entity;
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.DEATH_BEAM.get()) {
            if (!level.isClientSide && stack.getDamageValue() == 29) {
                player.getCooldowns().addCooldown(this, 100);
                stack.setDamageValue(stack.getDamageValue() - 30);
            }
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 5, 0, false, false));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack itemStack = player.getProjectile(stack);
        if (!world.isClientSide) {
            if (player.getTicksUsingItem() % fireRate == 0) {
                ItemStack ammo = new ItemStack(Items.AIR);
                AbstractArrow ammoEntity = new DeathBeamEntity(world, player, ammo);
                ammoEntity.shootFromRotation(player, player.getXRot(),player.getYRot(), 0F, 45, 1.5F);
                world.addFreshEntity(ammoEntity);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);
                stack.hurtAndBreak(1, player, (b) -> {
                });
                return InteractionResultHolder.consume(stack);
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return AMMO;
    }
}
