package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.LazerEntity;
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
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class Lazer extends BowItem {
    public static final Predicate<ItemStack> AMMO = stack -> stack.getItem() == Items.AIR;
    public Lazer() {
        super(new Properties().rarity(ExtraRarity.ULTIMATE).fireResistant().durability(60000));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player) entity;
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.LAZER.get()) {
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
            LazerEntity ammoEntity = new LazerEntity(level, player, ammo);
            ammoEntity.setNoGravity(true);
            ammoEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0.0F);
            level.addFreshEntity(ammoEntity);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1.0F, 1.0F);
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

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return AMMO;
    }
}
