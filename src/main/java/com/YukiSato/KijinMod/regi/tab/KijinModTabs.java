package com.YukiSato.KijinMod.regi.tab;

import com.YukiSato.KijinMod.main.KijinMod;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class KijinModTabs {

    public static final DeferredRegister<CreativeModeTab> MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KijinMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> KIJIN_MAIN = MOD_TABS.register("kijin_main",
            ()-> {return CreativeModeTab.builder()
                    .icon(()->new ItemStack(KijinModItems.KIJIN_INGOD.get()))
                    .title(Component.translatable("itemGroup.kijin_main"))
                    .displayItems((param, output)->{
                        for (Item item : KijinMain.items) {
                            output.accept(item);
                        }
                    })
                    .build();
    });

    public static final RegistryObject<CreativeModeTab> KIJIN_SUB = MOD_TABS.register("kijin_sub",
            ()-> {return CreativeModeTab.builder()
                    .icon(()->new ItemStack(Blocks.CHERRY_PLANKS))
                    .title(Component.translatable("itemGroup.kijin_sub"))
                    .withTabsBefore(KijinModTabs.KIJIN_MAIN.getId())
                    .displayItems((param, output)->{
                        for (Item item : KijinMain.items) {
                            output.accept(item);
                        }
                    })
                    .build();
            });
}
