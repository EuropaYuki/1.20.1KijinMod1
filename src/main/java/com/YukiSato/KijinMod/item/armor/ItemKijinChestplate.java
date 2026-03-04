package com.YukiSato.KijinMod.item.armor;

import com.YukiSato.KijinMod.effect.KijinEffects;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class ItemKijinChestplate extends ArmorItem {

    public static int set;
    public ItemKijinChestplate() {
        super(KijinArmorMaterials.KIJIN, Type.CHESTPLATE, new Properties().fireResistant().rarity(Rarity.EPIC));
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        Player player = (Player) entity;

        if (isWearingFullKijinArmor(player)) {
            set = 1;
            applyPotionEffects(player);
            applyEnchantments(player);
            player.removeEffect(MobEffects.DARKNESS);

            Vec3 vec3 = player.getLookAngle();
            if (KijinKeyBind.kijinKey[0].isDown()) {
                player.setDeltaMovement(vec3.x * 5, vec3.y * 5, vec3.z * 5);
            }
            if (KijinKeyBind.kijinKey[1].isDown()) {
                player.setDeltaMovement(vec3.x * 5, 0, vec3.z * 5);
            }
        } else {
            if (player.getAbilities().mayfly && !player.isCreative()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                set = 0;
            }
        }
    }

    private boolean isWearingFullKijinArmor(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() == KijinModItems.KIJIN_HELMET.get() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == KijinModItems.KIJIN_CHESTPLATE.get() &&
                player.getItemBySlot(EquipmentSlot.LEGS).getItem() == KijinModItems.KIJIN_LEGINS.get() && // `LEGINS` → `LEGGINGS` 修正
                player.getItemBySlot(EquipmentSlot.FEET).getItem() == KijinModItems.KIJIN_BOOTS.get();
    }

    private void applyPotionEffects(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 6, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, false));
    }

    private void applyEnchantments(Player player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack itemStack = player.getItemBySlot(slot);
                if (!itemStack.isEnchanted()) {
                    itemStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 10);
                }
            }
        }
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return set == 1;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return true;
    }
}
