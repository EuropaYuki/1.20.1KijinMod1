package com.YukiSato.KijinMod.entity;

import com.YukiSato.KijinMod.entity.custom.CameramanEntity;
import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
import com.YukiSato.KijinMod.main.KijinMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KijinMod.MOD_ID);

    public static final RegistryObject<EntityType<NinjaEntity>> NINJA =
            ENTITY_TYPES.register("ninja", () -> EntityType.Builder.of(NinjaEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 1.5F).build("ninja"));

    public static final RegistryObject<EntityType<CameramanEntity>> CAMERAMAN =
            ENTITY_TYPES.register("cameraman", () -> EntityType.Builder.of(CameramanEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 1.5F).build("cameraman"));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
