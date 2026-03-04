package com.YukiSato.KijinMod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class BlockKijinBlock extends Block {
    public BlockKijinBlock() {
        super(Properties.of()
                .strength(5.0F, 1500)
                .explosionResistance(1000F)
                .sound(SoundType.NETHER_BRICKS)
                .lightLevel((a) -> 15)
                .requiresCorrectToolForDrops()
        );
    }
}
