package com.YukiSato.KijinMod.regi;

import com.YukiSato.KijinMod.block.BlockKijinBlock;
import com.YukiSato.KijinMod.main.KijinMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class KijinModBlocks {

    public static class Blocks{

        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KijinMod.MOD_ID);
        public static final RegistryObject<Block> KIJIN_BLOCK = BLOCKS.register("kijin_block", BlockKijinBlock::new);
    }

    public static class BlockItems{
        public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KijinMod.MOD_ID);
        public static final RegistryObject<Item> KIJIN_BLOCK = BLOCK_ITEMS.register("kijin_block"
                , () -> new BlockItem(Blocks.KIJIN_BLOCK.get(), new Item.Properties().fireResistant()));
    }
}
