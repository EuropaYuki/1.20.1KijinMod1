package com.YukiSato.KijinMod.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Rarity;

public class ItemKijinBoots extends ArmorItem {
    public ItemKijinBoots() {
        super(KijinArmorMaterials.KIJIN, Type.BOOTS, new Properties().rarity(Rarity.EPIC).fireResistant());
    }
}
