package com.YukiSato.KijinMod.item.armor;


import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum KijinArmorMaterials implements ArmorMaterial {
    KIJIN("kijinmod:kijin", Integer.MAX_VALUE,new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 100, SoundEvents.ARMOR_EQUIP_NETHERITE,Float.MAX_VALUE , Float.MAX_VALUE, ()-> {return Ingredient.of(KijinModItems.KIJIN_INGOD.get());});
    private static final int[] HEALTH_FUNCTION_FOR_TYPE  = new int[] {1, 1, 1, 1};
    private final int durabilityMultiplier, enchantmentValue;
    private final int[] protectionFunctionForType;
    private final SoundEvent sound;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    private final String name;
    private final float toughness, knockbackResistance;

    private KijinArmorMaterials (String name, int durabilityMultiplier, int[] protectionFunctionForType, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionFunctionForType;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }
    public int getDurabilityForType(ArmorItem.Type p_266745_) {
        return HEALTH_FUNCTION_FOR_TYPE[p_266745_.getSlot().getIndex()] * this.durabilityMultiplier;
    }

    public int getDefenseForType(ArmorItem.Type p_266752_) {
        return protectionFunctionForType[p_266752_.getSlot().getIndex()];
    }
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public String getSerializedName() {
        return this.name;
    }
}
