package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.DemonsBolgEntity;
import com.YukiSato.KijinMod.entity.MauiBowEntity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class MauiBow extends BowItem {
    public boolean pulled = false;
    public MauiBow() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC).durability(9999));
    }

    @Override
    public void releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
        if (p_40669_ instanceof Player player) {
            ItemStack itemstack = Items.AIR.getDefaultInstance();

            int i = this.getUseDuration(p_40667_) - p_40670_;
            if (i < 0) return;
            float f = getPowerForTime(i);
            if (!((double)f < 0.1D)) {
                if (!p_40668_.isClientSide) {
                    AbstractArrow abstractarrow = new MauiBowEntity(p_40668_, player, itemstack);
                    abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 15.0F, 0.0F);
                    if (f == 1.0F) {
                        abstractarrow.setCritArrow(true);
                    }

                    int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, p_40667_);
                    if (j > 0) {
                        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                    }

                    int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, p_40667_);
                    if (k > 0) {
                        abstractarrow.setKnockback(k);
                    }

                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, p_40667_) > 0) {
                        abstractarrow.setSecondsOnFire(100);
                    }

                    p_40667_.hurtAndBreak(1, player, (p_289501_) -> {
                        p_289501_.broadcastBreakEvent(player.getUsedItemHand());
                    });

                    p_40668_.addFreshEntity(abstractarrow);
                }

                p_40668_.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1.0F, 1.0F / (p_40668_.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                p_40668_.addParticle(ParticleTypes.FLASH,player.getX(), player.getY(), player.getZ(), 0,0, 0);

                player.awardStat(Stats.ITEM_USED.get(this));

                pulled = false;
            }
        }
    }

    public static float getPowerForTime(int p_40662_) {
        float f = (float)p_40662_ / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        p_40673_.startUsingItem(p_40674_);
        pulled = true;
        if (p_40673_.isSteppingCarefully()) {
            itemstack.getEnchantmentTags().clear();
            itemstack.enchant(Enchantments.POWER_ARROWS, 13);
            itemstack.enchant(Enchantments.PUNCH_ARROWS, 10);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    public int getDefaultProjectileRange() {
        return 15;
    }
}
