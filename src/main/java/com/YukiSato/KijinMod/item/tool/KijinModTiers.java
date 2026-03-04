package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.main.KijinMod;
import com.YukiSato.KijinMod.regi.KijinModItems;
import com.YukiSato.KijinMod.regi.KijinModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class KijinModTiers {

    public static final ForgeTier SAKURITE = new ForgeTier(5, 5000, 13.0F, 5.0F, 100, KijinModTags.Blocks.NEEDS_SAKURITE_TOOL, () -> Ingredient.of(KijinModItems.KIJIN_INGOD.get()));

    static {
        TierSortingRegistry.registerTier(SAKURITE, new ResourceLocation(KijinMod.MOD_ID, "sakurite"), List.of(Tiers.NETHERITE), List.of());
    }
}
