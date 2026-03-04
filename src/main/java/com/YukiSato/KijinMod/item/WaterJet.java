package com.YukiSato.KijinMod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;


public class WaterJet extends Item {

    public WaterJet() {
        super(new Properties().durability(60000).rarity(ExtraRarity.ULTIMATE).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            BlockPos playerPos = player.blockPosition();
            // 半径30ブロックの範囲にTNTを敷き詰める
            int radius = 10;
            BlockState tntState = Blocks.LAVA.defaultBlockState();

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    // TNTを設置する位置を計算
                    BlockPos tntPos = playerPos.offset(x, 0, z);
                    // 上に何もない場合はTNTを設置
                    world.setBlock(tntPos, tntState, 3);
                }
            }
        }



        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }





}
