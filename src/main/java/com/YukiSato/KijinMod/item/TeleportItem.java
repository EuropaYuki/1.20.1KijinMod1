package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.KijinAmmoEntity;
import com.YukiSato.KijinMod.entity.SensarEntity;
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
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class TeleportItem extends BowItem {
    public static final Predicate<ItemStack> AMMO = stack -> stack.getItem() == Items.AIR;
    public TeleportItem() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player) entity;
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.TELEPORT_ITEM.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 5, 0, false, false));
        } else {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Vec3 vec3 = player.getLookAngle();
        ItemStack itemStack = player.getProjectile(stack);
        if (!level.isClientSide && itemStack.getItem() == Items.AIR) {
            ItemStack ammo = new ItemStack(Items.AIR);
            AbstractArrow ammoEntity = new SensarEntity(level, player, ammo);
            ammoEntity.shoot(vec3.x, vec3.y, vec3.z, 999F, 0F);
            level.addFreshEntity(ammoEntity);
            return InteractionResultHolder.consume(stack);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_FRAME_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResultHolder.fail(stack);
    }



    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return AMMO;
    }
}
