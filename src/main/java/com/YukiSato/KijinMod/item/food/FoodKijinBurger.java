package com.YukiSato.KijinMod.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class FoodKijinBurger extends Item {
    public FoodKijinBurger() {
        super(new Properties().food(new FoodProperties.Builder()
                .meat()
                .nutrition(10)
                .saturationMod(Float.MAX_VALUE)
                .fast()
                .build())
                .fireResistant().rarity(Rarity.EPIC));
    }
}
