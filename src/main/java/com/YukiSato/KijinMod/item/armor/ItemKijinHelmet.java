package com.YukiSato.KijinMod.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Rarity;

public class ItemKijinHelmet extends ArmorItem {
    public ItemKijinHelmet() {
        super(KijinArmorMaterials.KIJIN, Type.HELMET, new Properties().fireResistant().rarity(Rarity.EPIC));
    }
}
