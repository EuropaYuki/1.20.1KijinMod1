package com.YukiSato.KijinMod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class KijinEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "kijinmod");
    public static final RegistryObject<MobEffect> INVISIBILITY_EFFECT = EFFECTS.register("invisibility_effect", () -> new InvisibilityEffect());
    public static final RegistryObject<MobEffect> FREEZE_EFFECT = EFFECTS.register("freeze_effect", () -> new FreezeEffect());
}
