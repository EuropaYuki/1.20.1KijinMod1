package com.YukiSato.KijinMod.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Rarity;

public class ItemKijinLegins extends ArmorItem {
    public ItemKijinLegins() {
        super(KijinArmorMaterials.KIJIN, Type.LEGGINGS, new Properties().rarity(Rarity.EPIC).fireResistant());
    }
}
