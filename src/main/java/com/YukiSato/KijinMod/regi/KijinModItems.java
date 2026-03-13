package com.YukiSato.KijinMod.regi;

import com.YukiSato.KijinMod.entity.MobEntities;
import com.YukiSato.KijinMod.item.*;
import com.YukiSato.KijinMod.item.tool.DogiragonBaster;
import com.YukiSato.KijinMod.item.armor.ItemKijinBoots;
import com.YukiSato.KijinMod.item.armor.ItemKijinChestplate;
import com.YukiSato.KijinMod.item.armor.ItemKijinHelmet;
import com.YukiSato.KijinMod.item.armor.ItemKijinLegins;
import com.YukiSato.KijinMod.item.food.FoodKijinBurger;
import com.YukiSato.KijinMod.item.tool.*;
import com.YukiSato.KijinMod.main.KijinMod;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class KijinModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KijinMod.MOD_ID);

    public static final RegistryObject<Item> KIJIN_INGOD = ITEMS.register("kijin_ingod", ItemKijinInGod::new);
    public static final RegistryObject<Item> KIJIN_SWORD = ITEMS.register("kijin_sword", KijinSword::new);
    public static final RegistryObject<Item> KIJIN_BURGER = ITEMS.register("kijin_burger", FoodKijinBurger::new);
    public static final RegistryObject<Item> KIJIN_PICKAXE = ITEMS.register("kijin_pickaxe", KijinPickaxe::new);
    public static final RegistryObject<Item> KIJIN_GUN = ITEMS.register("matchlock_gun", MatchlockGun::new);
    public static final RegistryObject<Item> KIJIN_AMMO = ITEMS.register("kijin_ammo", ItemKijinAmmo::new);
    public static final RegistryObject<Item> KIJIN_BOW = ITEMS.register("kijin_bow", KijinBow::new);
    public static final RegistryObject<Item> KIJIN_HELMET = ITEMS.register("kijin_helmet", ItemKijinHelmet::new);
    public static final RegistryObject<Item> KIJIN_CHESTPLATE = ITEMS.register("kijin_chestplate", ItemKijinChestplate::new);
    public static final RegistryObject<Item> KIJIN_LEGINS = ITEMS.register("kijin_legins", ItemKijinLegins::new);
    public static final RegistryObject<Item> KIJIN_BOOTS = ITEMS.register("kijin_boots", ItemKijinBoots::new);
    public static final RegistryObject<Item> METAL_DETECTOR_ITEM = ITEMS.register("metal_detector_item", MetalDetectorItem::new);
    public static final RegistryObject<Item> MAUI_BOW = ITEMS.register("maui_bow", MauiBow::new);
    public static final RegistryObject<Item> NINJA_SPAWN_EGG = ITEMS.register("ninja_spawn_egg",
            () -> new ForgeSpawnEggItem(MobEntities.NINJA, 0x7e9680, 0xc5d1c5, new Item.Properties()));

    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", Hammer::new);
    public static final RegistryObject<Item> KIJIN_SHIELD = ITEMS.register("kijin_shield", KijinShield::new);
    public static final RegistryObject<Item> DEATH_BEAM = ITEMS.register("sobmasingun", () -> new Sobmasingun(100));
    public static final RegistryObject<Item> KIJIN_AXE = ITEMS.register("kijin_axe", KijinAxe::new);
    public static final RegistryObject<Item> KIJIN_SHOVEL = ITEMS.register("kijin_shovel", KijinShovel::new);
    public static final RegistryObject<Item> KIJIN_HOE = ITEMS.register("kijin_hoe", KijinHoe::new);
    public static final RegistryObject<Item> CAMERAMAN_SPAWN_EGG = ITEMS.register("cameraman_spawn_egg",
            () -> new ForgeSpawnEggItem(MobEntities.CAMERAMAN, 0x7e963, 0xc5d1c1, new Item.Properties()));
    public static final RegistryObject<Item> SPECKER = ITEMS.register("specker", Spercker::new);
    public static final RegistryObject<Item> LAZER = ITEMS.register("lazer", Lazer::new);
    public static final RegistryObject<Item> KNIFE = ITEMS.register("knife", Knife::new);
    public static final RegistryObject<Item> HINOKAMI = ITEMS.register("hinokami", Hinokami::new);
    public static final RegistryObject<Item> KAIOKEN = ITEMS.register("kaioken", Kaioken::new);
    public static final RegistryObject<Item> ZENO_WAND = ITEMS.register("zeno_wand", ZenoWand::new);
    public static final RegistryObject<Item> PSYCHOKINESIS = ITEMS.register("psychokinesis", PsychokinesisItem::new);
    public static final RegistryObject<Item> TIME_STOP_ITEM = ITEMS.register("time_stop_item", TimeStopItem::new);
    public static final RegistryObject<Item> SCOUTER = ITEMS.register("scouter", Scouter::new);
    public static final RegistryObject<Item> GOD_ITEM = ITEMS.register("god_item", GodItem::new);
    public static final RegistryObject<Item> STOP_WACH = ITEMS.register("stop_wach", StopWach::new);
    public static final RegistryObject<Item> SUPER_SAIYAN_ITEM = ITEMS.register("super_saiyan_item", SuperSaiyanItem::new);
    public static final RegistryObject<Item> ROCK_ROCKET_LAUNCHER = ITEMS.register("rock_rocket_launcher", RockRocketLauncher::new);
    public static final RegistryObject<Item> TELEPORT_ITEM = ITEMS.register("teleport_item", TeleportItem::new);
    public static final RegistryObject<Item> WATER_JET = ITEMS.register("water_jet", WaterJet::new);
    public static final RegistryObject<Item> DRAGON_SWORD = ITEMS.register("dragon_sword", DragonSword::new);
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", Ruby::new);
    public static final RegistryObject<Item> REVOLUTION_FIST = ITEMS.register("revolution_fist", RevolutionFist::new);
    public static final RegistryObject<Item> DOGIRAGON_BASTER = ITEMS.register("dogiragon_baster", DogiragonBaster::new);
    public static final RegistryObject<Item> BABY_JACK = ITEMS.register("baby_jack",
            () -> new BaByJack(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationMod(0.1f)
                            .alwaysEat()
                            .build())
                    .stacksTo(64)));
    public static final RegistryObject<Item> GUARANTEED_KILL_COMBO_HAPPY_SET_ITEM = ITEMS.register("guaranteed_kill_combo_happy_set_item", GuaranteedKillComboHappySetItem::new);
    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", TestItem::new);
    public static final RegistryObject<Item> MAGIC_STICK = ITEMS.register("magic_stick", FrierenMagicWand::new);
    public static final RegistryObject<Item> MAGIC_WAND = ITEMS.register("magic_wand", MagicWand::new);
    public static final RegistryObject<Item> REVOLTE_SWORD = ITEMS.register("revolte_sword", RevolteSword::new);
}
