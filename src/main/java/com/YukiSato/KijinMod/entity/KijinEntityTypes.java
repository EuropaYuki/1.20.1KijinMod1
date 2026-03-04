package com.YukiSato.KijinMod.entity;

import com.YukiSato.KijinMod.main.KijinMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class KijinEntityTypes {

    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "kijinmod");
    public static final RegistryObject<EntityType<DemonsBolgEntity>> DEMONS_BOLG = REGISTER.register("demons_bolg"
            , () -> EntityType.Builder.<DemonsBolgEntity>of(DemonsBolgEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(new ResourceLocation(KijinMod.MOD_ID, "demons_bolg").toString())
    );
    public static final RegistryObject<EntityType<KijinAmmoEntity>> KIJIN_AMMO = REGISTER.register("kijin_ammo"
            , () -> EntityType.Builder.<KijinAmmoEntity>of(KijinAmmoEntity::new, MobCategory.MISC)
                    .sized(0.2F, 0.2F)
                    .build(new ResourceLocation(KijinMod.MOD_ID, "kijin_ammo").toString())
    );

    public static final RegistryObject<EntityType<MauiBowEntity>> MAUI_BOW = REGISTER.register("maui_bow"
            , () -> EntityType.Builder.<MauiBowEntity>of(MauiBowEntity::new, MobCategory.MISC)
                    .sized(10F, 10F)
                    .build(new ResourceLocation(KijinMod.MOD_ID, "maui_bow").toString())
    );

    public static final RegistryObject<EntityType<DeathBeamEntity>> DEATH_BEAM = REGISTER.register("death_beam"
            , () -> EntityType.Builder.<DeathBeamEntity>of(DeathBeamEntity::new, MobCategory.MISC)
                    .sized(5F, 5F)
                    .build(new ResourceLocation(KijinMod.MOD_ID, "death_beam").toString())
    );
    public static final RegistryObject<EntityType<RocketLauncherEntity>> SPERCKER = REGISTER.register("spercker"
            , () -> EntityType.Builder.<RocketLauncherEntity>of(RocketLauncherEntity::new, MobCategory.MISC)
                    .sized(0.2F, 0.2F)
                    .build(new ResourceLocation(KijinMod.MOD_ID, "spercker").toString())
    );
    public static final RegistryObject<EntityType<LazerEntity>> LAZER = REGISTER.register("lazer"
            , () -> EntityType.Builder.<LazerEntity>of(LazerEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(new ResourceLocation(KijinMod.MOD_ID, "lazer").toString())
    );
    public static final RegistryObject<EntityType<SensarEntity>> SENSAR = REGISTER.register("sensar"
            , () -> EntityType.Builder.<SensarEntity>of(SensarEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(new ResourceLocation(KijinMod.MOD_ID, "sensar").toString())
    );


//    private static <T extends Entity>RegistryObject<EntityType<T>> register(String id, BiFunction<EntityType<T>, Level, T> function) {
//        EntityType<T> type = EntityType.Builder.of(function::apply, MobCategory.MISC)
//                .sized(0.5F, 0.5F)
//                .clientTrackingRange(100).fireImmune()
//                .setShouldReceiveVelocityUpdates(true).updateInterval(20)
//                .build(id);
//        return REGISTER.register(id, ()->type);
//    }
}
