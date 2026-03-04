package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.item.ExtraRarity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Knife extends SwordItem {
    public Knife() {
        super(KijinModTiers.SAKURITE, Integer.MAX_VALUE, 2, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE).durability(0));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player && target != null) {
            // ヘルスポイントを吸収し、攻撃者に転送
            float healthToAbsorb = 8.5F; // 吸収するヘルス量
            float targetHealth = target.getHealth();
            float transferHealth = Math.min(healthToAbsorb, targetHealth);
            target.hurt(target.damageSources().playerAttack((Player) attacker), transferHealth); // ターゲットからヘルスを減らす
            attacker.heal(healthToAbsorb);
            target.setHealth(0.0F);
            target.setHealth(20F);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (!level.isClientSide && KijinKeyBind.kijinKey[8].consumeClick()) {
            stack.getEnchantmentTags().clear();
            stack.enchant(Enchantments.SHARPNESS, 10);
            stack.enchant(Enchantments.FIRE_ASPECT, 5);
            stack.enchant(Enchantments.MOB_LOOTING, 5);
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Vec3 vec3 = player.getLookAngle();
        player.setDeltaMovement(vec3.x * 5, vec3.y * 5, vec3.z * 5);
        if (!level.isClientSide) {
            level.explode(player, player.getX() - 1, player.getY(), player.getZ() - 1, 5F, Level.ExplosionInteraction.MOB);
        }
        return InteractionResultHolder.consume(itemstack);
    }

}
