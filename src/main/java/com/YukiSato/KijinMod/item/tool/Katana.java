package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.item.ExtraRarity;
import net.minecraft.world.item.SwordItem;

public class Katana extends SwordItem {
    public Katana() {
        super(KijinModTiers.SAKURITE, 11, -2.6F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }
}
